package com.lihua.service.system.post;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.exception.ServiceException;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.entity.system.SysDept;
import com.lihua.entity.system.SysPost;
import com.lihua.mapper.system.SysDeptMapper;
import com.lihua.mapper.system.SysPostMapper;
import com.lihua.model.system.dto.SysPostDTO;
import com.lihua.model.system.vo.SysPostVO;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.dict.DictUtils;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.security.LoginUserContext;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Resource
    private SysPostMapper sysPostMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    // 校验手机号码
    private final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    // 校验邮箱
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");


    @Override
    public IPage<SysPostVO> queryPage(SysPostDTO dto) {
        IPage<SysPostVO> iPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<SysPost> queryWrapper = getQueryWrapper(dto);
        sysPostMapper.queryPage(iPage,queryWrapper);
        return iPage;
    }

    @Override
    public SysPost queryById(String id) {
        return sysPostMapper.selectById(id);
    }

    @Override
    public String savePost(SysPost sysPost) {
        checkPostCode(sysPost);
        if (!StringUtils.hasText(sysPost.getId())) {
            return insert(sysPost);
        }
        return update(sysPost);
    }

    private String insert(SysPost sysPost) {
        sysPost.setCreateTime(DateUtils.now());
        sysPost.setCreateId(LoginUserContext.getUserId());
        sysPost.setDelFlag("0");
        sysPostMapper.insert(sysPost);
        return sysPost.getId();
    }

    private String update(SysPost sysPost) {
        sysPost.setUpdateId(LoginUserContext.getUserId());
        sysPost.setUpdateTime(DateUtils.now());
        sysPostMapper.updateById(sysPost);
        return sysPost.getId();
    }

    @Override
    public void deleteByIds(List<String> ids) {
        // 数据是否为停用状态
        checkStatus(ids);
        // 数据下没有分配用户
        checkUser(ids);
        sysPostMapper.deleteByIds(ids);
    }

    private void checkStatus(List<String> ids) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPost::getStatus,"0")
                .in(SysPost::getId,ids);
        Long count = sysPostMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("岗位状态为正常不允许删除");
        }
    }

    private void checkUser(List<String> ids) {
        Long count = sysPostMapper.postUserCount(ids);
        if (count > 0) {
            throw new ServiceException("存在用户不允许删除");
        }
    }

    @Override
    public List<SysPost> queryPostByDeptId(List<String> deptIdList) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysPost::getDeptId,deptIdList)
                .orderByAsc(SysPost::getSort);
        return sysPostMapper.selectList(queryWrapper);
    }

    @Override
    public Long queryCountByDeptId(List<String> deptIdList) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysPost::getDeptId,deptIdList);
        return sysPostMapper.selectCount(queryWrapper);
    }

    @Override
    public Map<String, List<SysPost>> getPostOptionByDeptId(List<String> deptIds) {
        QueryWrapper<SysPost> deptQueryWrapper = new QueryWrapper<>();
        deptQueryWrapper.lambda()
                .in(SysPost::getDeptId, deptIds)
                .eq(SysPost::getStatus,"0")
                .orderByAsc(SysPost::getSort);
        List<SysPost> sysPosts = sysPostMapper.selectList(deptQueryWrapper);

        return sysPosts.stream().collect(Collectors.groupingBy(SysPost::getDeptId));
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysPost> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysPost::getStatus, status)
                .set(SysPost::getUpdateId, LoginUserContext.getUserId())
                .set(SysPost::getUpdateTime, DateUtils.now())
                .eq(SysPost::getId, id);
        sysPostMapper.update(null, updateWrapper);
        return status;
    }

    @Override
    public String exportExcel(SysPostDTO dto) {
        QueryWrapper<SysPost> queryWrapper = getQueryWrapper(dto);
        List<SysPostVO> sysPostVOList = sysPostMapper.queryPage(queryWrapper);
        return ExcelUtils.excelExport(sysPostVOList, SysPostVO.class, "系统岗位");
    }

    @Override
    public ExcelImportResult importExcel(List<SysPostVO> sysPostVOList) {
        // 无法倒入的部门列表
        List<SysPostVO> errorPostVos = new ArrayList<>();
        // 可导入的部门列表
        List<SysPostVO> importPostVos;

        // sysPostVOList 中存在的重复数据(code)
        Set<String> postCodeRepeatSet = new HashSet<>();
        // 记录重复数据
        recordRepeatData(sysPostVOList, postCodeRepeatSet);
        // 数据库中数据（编码、部门名称-id map）
        Set<String> postCodeDbSet = new HashSet<>();
        Map<String,String> deptNameIdMap = new HashMap<>();
        Map<String,String> deptNameCodeMap = new HashMap<>();
        getDbPostData(sysPostVOList, postCodeDbSet, deptNameIdMap, deptNameCodeMap);

        // 用到的字典数据
        List<SysDictDataVO> sysStatus = DictUtils.getDictData("sys_status");
        String statusJoin = sysStatus.stream().map(SysDictDataVO::getLabel).collect(Collectors.joining("、"));
        // 开始过滤数据
        importPostVos = sysPostVOList.stream().filter(sysPostVO -> {

            // 过滤岗位编码（excel内重复、数据库已存在、未填写）
            boolean filterPostCode = filterPostCode(sysPostVO, errorPostVos, postCodeRepeatSet, postCodeDbSet);
            if (!filterPostCode) {
                return false;
            }

            // 过滤岗位名称（未填写）
            boolean filterPostName = filterPostName(sysPostVO, errorPostVos);
            if (!filterPostName) {
                return false;
            }

            // 过滤所属部门（未填写、数据库不存在），已存在的赋值deptId
            boolean filterDeptName = filterDeptName(sysPostVO, errorPostVos,  deptNameIdMap, deptNameCodeMap);
            if (!filterDeptName) {
                return false;
            }

            // 过滤状态（填写错误，未填写）
            boolean filterStatus = filterStatus(sysPostVO, errorPostVos, sysStatus, statusJoin);
            if (!filterStatus) {
                return false;
            }

            // 过滤手机号码（格式）
            boolean filterPhoneNumber = filterPhoneNumber(sysPostVO, errorPostVos);
            if (!filterPhoneNumber) {
                return false;
            }

            // 过滤邮箱（格式）
            return filterEmail(sysPostVO, errorPostVos);
        }).toList();

        // 处理完毕后获得两批数据：通过校验可导入 / 数据存在异常需用户重新处理
        // 导出错误数据集
        String errExcelPath = null;
        if (!errorPostVos.isEmpty()) {
            String errExcelName = LoginUserContext.getUserId() + "_导入失败_" + UUID.randomUUID().toString().replace("-","");
            // 导出excel
            errExcelPath = ExcelUtils.excelExport(errorPostVos, SysPostVO.class, errExcelName);
        }

        // 插入数据
        if (!importPostVos.isEmpty()) {
            batchInsert(importPostVos);
        }

        // 返回汇总的导入结果
        return new ExcelImportResult(sysPostVOList.size() == importPostVos.size(),
                sysPostVOList.size(),
                importPostVos.size(),
                errorPostVos.size(),
                errExcelPath);
    }

    // 批量插入
    private void batchInsert(List<SysPostVO> importPostVos) {
        LocalDateTime now = DateUtils.now();
        List<SysPost> sysPostList = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);
        // 创建岗位对象并生成id
        importPostVos.forEach(sysPostVO -> {
            SysPost sysPost = new SysPost();
            BeanUtils.copyProperties(sysPostVO, sysPost);
            String id = String.valueOf(IdWorker.getId(sysPost));
            sysPost.setId(id);
            sysPost.setDelFlag("0");
            sysPost.setCreateTime(now);
            sysPost.setCreateId(LoginUserContext.getUserId());
            sysPost.setSort(index.get());
            index.getAndIncrement();
            sysPostList.add(sysPost);
        });

        // 批量插入
        SysPostServiceImpl sysPostService = (SysPostServiceImpl) AopContext.currentProxy();
        sysPostService.saveBatch(sysPostList);
    }

    // 过滤邮箱
    private boolean filterEmail(SysPostVO sysPostVO, List<SysPostVO> errorPostVos) {
        String email = sysPostVO.getEmail();
        if (StringUtils.hasText(email)) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                sysPostVO.setImportErrorMsg("邮箱格式错误，请检查数据");
                errorPostVos.add(sysPostVO);
                return false;
            }
        }
        return true;
    }

    // 过滤手机号码
    private boolean filterPhoneNumber(SysPostVO sysPostVO, List<SysPostVO> errorPostVos) {
        String phoneNumber = sysPostVO.getPhoneNumber();
        if (StringUtils.hasText(phoneNumber)) {
            if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
                sysPostVO.setImportErrorMsg("手机号码格式错误，请检查数据");
                errorPostVos.add(sysPostVO);
                return false;
            }
        }

        return true;
    }


    // 过滤岗位
    private boolean filterStatus(SysPostVO sysPostVO, List<SysPostVO> errorPostVos, List<SysDictDataVO> sysStatus, String statusJoin) {
        List<SysDictDataVO> status = sysStatus.stream().filter(ug -> ug.getLabel().equals(sysPostVO.getStatus())).toList();
        if (status.isEmpty()) {
            sysPostVO.setImportErrorMsg("请填写岗位状态或岗位状态不合法，可输入项为：" + statusJoin);
            errorPostVos.add(sysPostVO);
            return false;
        }
        sysPostVO.setStatus(status.get(0).getValue());
        return true;
    }

    // 过滤deptName
    private boolean filterDeptName(SysPostVO sysPostVO, List<SysPostVO> errorPostVos, Map<String, String> deptNameIdMap, Map<String, String> deptNameCodeMap) {
        String deptName = sysPostVO.getDeptName();

        if (!StringUtils.hasText(deptName)) {
            sysPostVO.setImportErrorMsg("请填写部门名称");
            errorPostVos.add(sysPostVO);
            return false;
        }

        String deptId = deptNameIdMap.get(deptName);
        String deptCode = deptNameCodeMap.get(deptName);
        if (!StringUtils.hasText(deptId) || !StringUtils.hasText(deptCode)) {
            sysPostVO.setImportErrorMsg("岗位所在部门不存在，请检查部门数据");
            errorPostVos.add(sysPostVO);
            return false;
        }

        // 赋值deptId/deptCode
        sysPostVO.setDeptId(deptId);
        sysPostVO.setDeptCode(deptCode);
        return true;
    }

    //  过滤岗位名称
    private boolean filterPostName(SysPostVO sysPostVO, List<SysPostVO> errorPostVos) {
        if (!StringUtils.hasText(sysPostVO.getName())) {
            sysPostVO.setImportErrorMsg("请填写岗位名称");
            errorPostVos.add(sysPostVO);
            return false;
        }
        return true;
    }

    // 过滤岗位编码
    private boolean filterPostCode(SysPostVO sysPostVO, List<SysPostVO> errorPostVos, Set<String> postCodeRepeatSet, Set<String> postCodeDbSet) {
        String code = sysPostVO.getCode();
        if (!StringUtils.hasText(code)) {
            sysPostVO.setImportErrorMsg("请填写岗位编码");
            errorPostVos.add(sysPostVO);
            return false;
        }
        if (postCodeRepeatSet.contains(code)) {
            sysPostVO.setImportErrorMsg("当前excel附件中岗位编码：" + code + " 重复，请检查");
            errorPostVos.add(sysPostVO);
            return false;
        }
        if (postCodeDbSet.contains(code)) {
            sysPostVO.setImportErrorMsg("岗位编码已存在");
            errorPostVos.add(sysPostVO);
            return false;
        }
        return true;
    }

    private void getDbPostData(List<SysPostVO> sysPostVOList, Set<String> postCodeDbSet, Map<String,String> deptNameIdMap, Map<String,String> deptNameCodeMap) {
        Set<String> collectPostCode = new HashSet<>();
        Set<String> collectDeptName = new HashSet<>();
        // 收集 postCode 和 deptName
        sysPostVOList.forEach(sysPostVO -> {
            collectPostCode.add(sysPostVO.getCode());
            collectDeptName.add(sysPostVO.getDeptName());
        });

        if (collectPostCode.isEmpty()) {
            throw new ServiceException("当前excel中岗位编码为空，请检查数据");
        }
        // 查询数据库中的code
        QueryWrapper<SysPost> postWrapper = new QueryWrapper<>();
        postWrapper.lambda().in(SysPost::getCode, collectPostCode)
                        .eq(SysPost::getDelFlag, "0")
                        .select(SysPost::getCode);
        List<SysPost> sysPosts = sysPostMapper.selectList(postWrapper);
        // 向外部数据赋值
        postCodeDbSet.addAll(sysPosts.stream().map(SysPost::getCode).collect(Collectors.toSet()));

        // 查询数据库中的deptName
        QueryWrapper<SysDept> deptWrapper = new QueryWrapper<>();
        deptWrapper.lambda()
                .in(SysDept::getName, collectDeptName)
                .eq(SysDept::getDelFlag, "0")
                .select(SysDept::getId, SysDept::getName, SysDept::getCode);
        List<SysDept> sysDeptList = sysDeptMapper.selectList(deptWrapper);
        // 向外部数据复制
        deptNameIdMap.putAll(sysDeptList.stream().collect(Collectors.toMap(SysDept::getName, SysDept::getId)));
        deptNameCodeMap.putAll(sysDeptList.stream().collect(Collectors.toMap(SysDept::getName, SysDept::getCode)));
    }

    // 记录重复的code数据
    private void recordRepeatData(List<SysPostVO> sysPostVOList, Set<String> postCodeRepeatSet) {
        Map<String, Integer> postCodeMap = new HashMap<>();

        sysPostVOList.forEach(sysPostVO -> {
            String code = sysPostVO.getCode();
            postCodeMap.put(code, postCodeMap.getOrDefault(code,0) + 1);
            Integer codeCount = postCodeMap.get(code);
            if (codeCount > 1) {
                postCodeRepeatSet.add(code);
            }
        });
    }

    /**
     * 获取查询列表/导出excel相关的QueryWrapper
     */
    private QueryWrapper<SysPost> getQueryWrapper(SysPostDTO dto) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();

        // 岗位名称
        if (StringUtils.hasText(dto.getName())) {
            queryWrapper.like("sys_post.name",dto.getName());
        }
        // 岗位编码
        if (StringUtils.hasText(dto.getCode())) {
            queryWrapper.like("sys_post.code",dto.getCode());
        }
        // 岗位状态
        if (StringUtils.hasText(dto.getStatus())) {
            queryWrapper.like("sys_post.status",dto.getStatus());
        }
        // 所属单位
        if (StringUtils.hasText(dto.getDeptId())) {
            queryWrapper.like("sys_post.dept_id",dto.getDeptId());
        }
        queryWrapper.eq("sys_post.del_flag", "0");
        queryWrapper
                .lambda()
                .orderByAsc(SysPost::getSort);
        return queryWrapper;
    }

    private void checkPostCode(SysPost sysPost) {
        QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(SysPost::getCode, sysPost.getCode());
        List<SysPost> sysPosts = sysPostMapper.selectList(queryWrapper);

        if (sysPosts.isEmpty()) {
            return;
        }

        if (sysPosts.size() > 1) {
            throw new ServiceException("岗位编码已存在");
        }

        if (!sysPosts.get(0).getId().equals(sysPost.getId())) {
            throw new ServiceException("岗位编码已存在");
        }
    }
}
