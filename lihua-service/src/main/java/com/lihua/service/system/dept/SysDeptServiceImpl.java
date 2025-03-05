package com.lihua.service.system.dept;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lihua.exception.ServiceException;
import com.lihua.model.dict.SysDictDataVO;
import com.lihua.model.excel.ExcelImportResult;
import com.lihua.entity.system.SysDept;
import com.lihua.entity.system.SysPost;
import com.lihua.mapper.system.SysDeptMapper;
import com.lihua.model.system.vo.SysDeptVO;
import com.lihua.service.system.post.SysPostService;
import com.lihua.utils.date.DateUtils;
import com.lihua.utils.dict.DictUtils;
import com.lihua.utils.excel.ExcelUtils;
import com.lihua.utils.security.LoginUserContext;
import com.lihua.utils.tree.TreeUtils;
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
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysPostService sysPostService;

    // 校验手机号码
    private final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    // 校验邮箱
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");


    @Override
    public List<SysDept> queryList(SysDept sysDept) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        // 部门名称
        if (StringUtils.hasText(sysDept.getName())) {
            queryWrapper.lambda().like(SysDept::getName,sysDept.getName());
        }
        // 部门编码
        if (StringUtils.hasText(sysDept.getCode())) {
            queryWrapper.lambda().like(SysDept::getCode,sysDept.getCode());
        }
        // 状态
        if (StringUtils.hasText(sysDept.getStatus())) {
            queryWrapper.lambda().eq(SysDept::getStatus,sysDept.getStatus());
        }
        queryWrapper.lambda().orderByAsc(SysDept::getSort);
        return sysDeptMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysDeptVO> queryDeptPostList(SysDept sysDept) {
        // 查询 dept 数据
        List<SysDept> sysDeptList = queryList(sysDept);

        if (sysDeptList.isEmpty()) {
            return new ArrayList<>();
        }

        List<SysDeptVO> sysDeptVOS = new ArrayList<>();

        // 根据dept id 查询岗位数据
        List<String> deptIds = sysDeptList.stream().map(SysDept::getId).toList();
        List<SysPost> postByDeptIdList = sysPostService.queryPostByDeptId(deptIds);

        // 部门岗位数据组合
        Map<String, List<SysPost>> postListGroupByDeptId =
                postByDeptIdList.stream().collect(Collectors.groupingBy(SysPost::getDeptId));

        sysDeptList.forEach(dept -> {
            SysDeptVO sysDeptVO = new SysDeptVO();
            BeanUtils.copyProperties(dept,sysDeptVO);
            if (postListGroupByDeptId.get(dept.getId()) != null) {
                sysDeptVO.setSysPostList(postListGroupByDeptId.get(dept.getId()));
            }
            sysDeptVOS.add(sysDeptVO);
        });

        return sysDeptVOS;
    }

    @Override
    public String saveDept(SysDept sysDept) {
        checkDeptCode(sysDept);
        checkDeptName(sysDept);
        if (StringUtils.hasText(sysDept.getId())) {
            return update(sysDept);
        } else {
            return insert(sysDept);
        }
    }

    private String update(SysDept sysDept) {
        sysDept.setUpdateId(LoginUserContext.getUserId());
        sysDept.setUpdateTime(DateUtils.now());
        sysDeptMapper.updateById(sysDept);
        return sysDept.getId();
    }

    private String insert(SysDept sysDept) {
        sysDept.setCreateId(LoginUserContext.getUserId());
        sysDept.setCreateTime(DateUtils.now());
        sysDept.setDelFlag("0");
        sysDeptMapper.insert(sysDept);
        return sysDept.getId();
    }

    @Override
    public SysDept queryById(String id) {
        return sysDeptMapper.selectById(id);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        // 数据是否为停用状态
        checkStatus(ids);
        // 数据没有子集
        checkChildren(ids);
        // 数据下没有岗位
        checkPost(ids);
        // 数据下没有分配用户
        checkUser(ids);

        sysDeptMapper.deleteByIds(ids);
    }

    @Override
    public List<SysDept> deptTreeOption() {
        List<SysDept> list = queryList(new SysDept());
        return TreeUtils.buildTree(list);
    }

    @Override
    public String updateStatus(String id, String currentStatus) {
        UpdateWrapper<SysDept> updateWrapper = new UpdateWrapper<>();
        String status = "0".equals(currentStatus) ? "1" : "0";

        updateWrapper.lambda()
                .set(SysDept::getStatus, status)
                .set(SysDept::getUpdateId, LoginUserContext.getUserId())
                .set(SysDept::getUpdateTime, DateUtils.now())
                .eq(SysDept::getId, id);
        sysDeptMapper.update(null, updateWrapper);
        return status;
    }

    @Override
    public String exportExcel(SysDept sysDept) {
        // 查询部门岗位信息
        List<SysDeptVO> deptPostList = queryDeptPostList(sysDept);
        // 处理生成路径名称和岗位名称拼接
        deptPostList.forEach(post -> {
            // 路径名称
            String namePath = getNamePath(deptPostList, post.getId());
            post.setNamePath(namePath);
            // 岗位名称
            if (post.getSysPostList() != null && !post.getSysPostList().isEmpty()) {
                List<String> postNameList = post.getSysPostList().stream().map(SysPost::getName).toList();
                post.setPostNames(String.join("、", postNameList));
            }
        });

        // 返回附件路径
        return ExcelUtils.excelExport(deptPostList, SysDeptVO.class, "系统部门");
    }

    @Override
    public ExcelImportResult importExcel(List<SysDeptVO> sysDeptVOS) {
        // 无法倒入的部门列表
        List<SysDeptVO> errorDeptVos = new ArrayList<>();
        // 可导入的部门列表
        List<SysDeptVO> importDeptVos;

        // sysDeptVOS中存在的重复数据（name、code）
        Set<String> deptNameRepeatSet = new HashSet<>();
        Set<String> deptCodeRepeatSet = new HashSet<>();
        // 记录重复数据
        recordRepeatData(sysDeptVOS, deptNameRepeatSet, deptCodeRepeatSet);

        // 数据库中数据
        Set<String> deptNameDbSet = new HashSet<>();
        Set<String> deptCodeDbSet = new HashSet<>();
        getDbDeptData(sysDeptVOS, deptNameDbSet, deptCodeDbSet);

        // 用到的字典数据
        List<SysDictDataVO> sysStatus = DictUtils.getDictData("sys_status");
        String statusJoin = sysStatus.stream().map(SysDictDataVO::getLabel).collect(Collectors.joining("、"));

        // 开始数据过滤
        importDeptVos = sysDeptVOS.stream().filter(sysDeptVO -> {

            // 过滤重复数据（部门名称、编码）
            boolean filterRepeatData = filterRepeatData(sysDeptVO, errorDeptVos, deptNameRepeatSet, deptCodeRepeatSet);
            if (!filterRepeatData) {
                return false;
            }

            // 过滤部门名称（已存在，未填写）
            boolean filterDeptName = filterDeptName(sysDeptVO, errorDeptVos, deptNameDbSet);
            if (!filterDeptName) {
                return false;
            }

            // 过滤部门编码（已存在，未填写）
            boolean filterDeptCode = filterDeptCode(sysDeptVO, errorDeptVos, deptCodeDbSet);
            if (!filterDeptCode) {
                return false;
            }

            // 过滤状态（填写错误，未填写）
            boolean filterStatus = filterStatus(sysDeptVO, errorDeptVos, sysStatus, statusJoin);
            if (!filterStatus) {
                return false;
            }

            // 过滤手机号码（格式）
            boolean filterPhoneNumber = filterPhoneNumber(sysDeptVO, errorDeptVos);
            if (!filterPhoneNumber) {
                return false;
            }

            // 过滤邮箱（格式）
            boolean filterEmail = filterEmail(sysDeptVO, errorDeptVos);
            if (!filterEmail) {
                return false;
            }

            // 过滤namePath（验证部门名称路径的最后是否为部门名称）
            return filterNamePath(sysDeptVO, errorDeptVos);
        }).toList();

        // 获取数据库中全部父级name-id
        List<SysDept> dbParentDeptList = getAllDbParentDeptList(importDeptVos);
        // 分配上级部门
        importDeptVos = filterParentDept(importDeptVos, dbParentDeptList, errorDeptVos);

        // 处理完毕后获得两批数据：通过校验可导入 / 数据存在异常需用户重新处理
        // 导出错误数据集
        String errExcelPath = null;
        if (!errorDeptVos.isEmpty()) {
            String errExcelName = LoginUserContext.getUserId() + "_导入失败_" + UUID.randomUUID().toString().replace("-","");
            // 导出excel
            errExcelPath = ExcelUtils.excelExport(errorDeptVos, SysDeptVO.class, errExcelName);
        }

        // 插入数据
        if (!importDeptVos.isEmpty()) {
            batchInsert(importDeptVos);
        }

        // 返回汇总的导入结果
        return new ExcelImportResult(sysDeptVOS.size() == importDeptVos.size(),
                sysDeptVOS.size(),
                importDeptVos.size(),
                errorDeptVos.size(),
                errExcelPath);
    }

    // 批量插入
    private void batchInsert(List<SysDeptVO> importDeptVos) {
        List<SysDept> sysDeptList = new ArrayList<>();
        Map<String, SysDept> deptNameToDeptMap = new HashMap<>();
        LocalDateTime now = DateUtils.now();
        // 创建部门对象并生成 ID
        AtomicInteger index = new AtomicInteger(1);
        importDeptVos.forEach(deptVO -> {
            SysDept sysDept = new SysDept();
            BeanUtils.copyProperties(deptVO, sysDept);
            String id = String.valueOf(IdWorker.getId(sysDept));
            sysDept.setId(id);
            sysDept.setDelFlag("0");
            sysDept.setCreateTime(now);
            sysDept.setCreateId(LoginUserContext.getUserId());
            sysDept.setSort(index.get());
            sysDeptList.add(sysDept);
            deptNameToDeptMap.put(sysDept.getName(), sysDept);
            index.getAndIncrement();
        });

        // 设置父级 ID
        importDeptVos.forEach(deptVO -> {
            String[] allParentArray = deptVO.getNamePath().split("/");
            if (allParentArray.length > 1) {
                String parentName = allParentArray[allParentArray.length - 2];
                SysDept parentDept = deptNameToDeptMap.get(parentName);
                if (parentDept != null && !StringUtils.hasText(deptVO.getParentId())) {
                    SysDept currentDept = deptNameToDeptMap.get(deptVO.getName());
                    if (currentDept != null) {
                        currentDept.setParentId(parentDept.getId());
                    }
                }
            } else {
                // 如果没有父级，则设置为根节点
                SysDept currentDept = deptNameToDeptMap.get(deptVO.getName());
                if (currentDept != null) {
                    currentDept.setParentId("0");
                }
            }
        });

        // 批量插入
        SysDeptServiceImpl sysDeptService = (SysDeptServiceImpl) AopContext.currentProxy();
        sysDeptService.saveBatch(sysDeptList);
    }

    // 分配上级部门id
    private List<SysDeptVO> filterParentDept(List<SysDeptVO> importDeptVos, List<SysDept> dbParentDeptList, List<SysDeptVO> errorDeptVos) {
        // 使用 Map 存储数据库中的部门名称和对应的 ID
        Map<String, String> dbParentMap = dbParentDeptList.stream()
                .collect(Collectors.toMap(SysDept::getName, SysDept::getId));

        // 提取所有待匹配的部门名称集合
        Set<String> nameSet = importDeptVos.stream().map(SysDeptVO::getName).collect(Collectors.toSet());

        // 有问题的部门名称
        Set<String> errorParentNameSet = new HashSet<>();

        // 分配 parentId 并标记异常部门
        importDeptVos.forEach(deptVO -> {
            String namePath = deptVO.getNamePath();
            String[] parentArray = namePath.split("/");
            String parentName = parentArray.length > 1 ? parentArray[parentArray.length - 2] : null;

            if (namePath.equals(deptVO.getName())) {
                // 根节点
                deptVO.setParentId("0");
            } else if (parentName != null && dbParentMap.containsKey(parentName)) {
                // 数据库中存在的上级部门
                deptVO.setParentId(dbParentMap.get(parentName));
            } else if (parentName != null && !nameSet.contains(parentName)) {
                // 异常部门
                errorParentNameSet.add(parentName);
            }
        });

        // 最终过滤掉父级部门异常的数据
        return importDeptVos.stream().filter(deptVO -> {
            if (StringUtils.hasText(deptVO.getParentId())) {
                return true;
            }

            String namePath = deptVO.getNamePath();
            for (String errorParentName : errorParentNameSet) {
                if (namePath.contains(errorParentName)) {
                    deptVO.setImportErrorMsg("父级部门 " + errorParentName + " 存在异常或不存在，请检查对应父级部门数据");
                    errorDeptVos.add(deptVO);
                    return false;
                }
            }
            return true;
        }).toList();
    }

    // 获取全部数据库中存在的父级id
    private List<SysDept> getAllDbParentDeptList(List<SysDeptVO> importDeptVos) {

        if (importDeptVos.isEmpty()) {
            return new ArrayList<>();
        }

        // 将所有parentName取出放入set集合
        Set<String> parentNameSet = new HashSet<>();
        List<String> list = importDeptVos.stream().map(SysDeptVO::getNamePath).toList();
        list.forEach(item -> {
            String[] parentNameArray = item.split("/");
            parentNameSet.addAll(Arrays.asList(parentNameArray));
        });

        // 数据库中查询出已保存的父级部门
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SysDept::getName, parentNameSet)
                        .eq(SysDept::getDelFlag, "0")
                        .select(SysDept::getId, SysDept::getName);

        return sysDeptMapper.selectList(wrapper);
    }

    // 过滤验证部门名称路径
    private boolean filterNamePath(SysDeptVO sysDeptVO, List<SysDeptVO> errorDeptVos) {
        String namePath = sysDeptVO.getNamePath();

        if (!StringUtils.hasText(namePath)) {
            sysDeptVO.setImportErrorMsg("部门名称路径为空，请检查数据");
            errorDeptVos.add(sysDeptVO);
            return false;
        }

        if (!namePath.endsWith(sysDeptVO.getName())) {
            sysDeptVO.setImportErrorMsg("部门名称路径最后一级必须为当前部门名称，请检查数据");
            errorDeptVos.add(sysDeptVO);
            return false;
        }

        return true;
    }

    // 过滤邮箱
    private boolean filterEmail(SysDeptVO sysDeptVO, List<SysDeptVO> errorDeptVos) {
        String email = sysDeptVO.getEmail();
        if (StringUtils.hasText(email)) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                sysDeptVO.setImportErrorMsg("邮箱格式错误，请检查数据");
                errorDeptVos.add(sysDeptVO);
                return false;
            }
        }
        return true;
    }

    // 过滤手机号码
    private boolean filterPhoneNumber(SysDeptVO sysDeptVO, List<SysDeptVO> errorDeptVos) {
        String phoneNumber = sysDeptVO.getPhoneNumber();
        if (StringUtils.hasText(phoneNumber)) {
            if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
                sysDeptVO.setImportErrorMsg("手机号码格式错误，请检查数据");
                errorDeptVos.add(sysDeptVO);
                return false;
            }
        }

        return true;
    }

    // 过滤状态
    private boolean filterStatus(SysDeptVO sysDeptVO, List<SysDeptVO> errorDeptVos, List<SysDictDataVO> sysStatus, String statusJoin) {
        List<SysDictDataVO> status = sysStatus.stream().filter(ug -> ug.getLabel().equals(sysDeptVO.getStatus())).toList();
        if (status.isEmpty()) {
            sysDeptVO.setImportErrorMsg("请填写部门状态或部门状态不合法，可输入项为：" + statusJoin);
            errorDeptVos.add(sysDeptVO);
            return false;
        }
        sysDeptVO.setStatus(status.get(0).getValue());
        return true;
    }

    // 过滤部门名称
    private boolean filterDeptName(SysDeptVO sysDeptVO, List<SysDeptVO> errorDeptVos, Set<String> deptNameRepeatSet) {
        if (!StringUtils.hasText(sysDeptVO.getName())) {
            sysDeptVO.setImportErrorMsg("请填写部门名称");
            errorDeptVos.add(sysDeptVO);
            return false;
        }
        if (deptNameRepeatSet.contains(sysDeptVO.getName())) {
            sysDeptVO.setImportErrorMsg("部门名称已存在");
            errorDeptVos.add(sysDeptVO);
            return false;
        }
        return true;
    }

    // 过滤部门编码
    private boolean filterDeptCode(SysDeptVO sysDeptVO, List<SysDeptVO> errorDeptVos, Set<String> deptCodeDbSet) {
        if (!StringUtils.hasText(sysDeptVO.getCode())) {
            sysDeptVO.setImportErrorMsg("请填写部门编码");
            errorDeptVos.add(sysDeptVO);
            return false;
        }
        if (deptCodeDbSet.contains(sysDeptVO.getCode())) {
            sysDeptVO.setImportErrorMsg("部门编码已存在");
            errorDeptVos.add(sysDeptVO);
            return false;
        }
        return true;
    }


    // 获取数据库中部门数据
    private void getDbDeptData(List<SysDeptVO> sysDeptVOS, Set<String> deptNameDbSet, Set<String> deptCodeDbSet) {
        Set<String> collectDeptName = new HashSet<>();
        Set<String> collectDeptCode = new HashSet<>();
        sysDeptVOS.forEach(sysDeptVO -> {
            collectDeptName.add(sysDeptVO.getName());
            collectDeptCode.add(sysDeptVO.getCode());
        });

        if (collectDeptName.isEmpty()) {
            throw new ServiceException("当前excel中部门名称为空，请检查数据");
        }
        if (collectDeptCode.isEmpty()) {
            throw new ServiceException("当前excel中部门编码为空，请检查数据");
        }

        // 查询数据库相关数据
        deptNameDbSet.addAll(sysDeptMapper.queryDeptNameByNames(collectDeptName));
        deptCodeDbSet.addAll(sysDeptMapper.queryDeptCodeByCodes(collectDeptCode));
    }

    // 过滤表格内重复数据
    private boolean filterRepeatData(SysDeptVO sysDeptVO, List<SysDeptVO> errorDeptVos, Set<String> deptNameRepeatSet, Set<String> deptCodeRepeatSet) {
        String name = sysDeptVO.getName();
        String code = sysDeptVO.getCode();

        if (deptNameRepeatSet.contains(name)) {
            sysDeptVO.setImportErrorMsg("当前excel附件中部门名称：" + name + " 重复，请检查");
            errorDeptVos.add(sysDeptVO);
            return false;
        }

        if (deptCodeRepeatSet.contains(code)) {
            sysDeptVO.setImportErrorMsg("当前excel附件中部门编码：" + code + " 重复，请检查");
            errorDeptVos.add(sysDeptVO);
            return false;
        }

        return true;
    }

    // 记录重复数据
    private void recordRepeatData(List<SysDeptVO> sysDeptVOS, Set<String> deptNameRepeatSet, Set<String> deptCodeRepeatSet) {
        // 使用map进行计数
        Map<String, Integer> deptNameMap = new HashMap<>();
        Map<String, Integer> deptCodeMap = new HashMap<>();
        sysDeptVOS.forEach(sysDeptVO -> {
            String name = sysDeptVO.getName();
            String code = sysDeptVO.getCode();
            deptNameMap.put(name, deptNameMap.getOrDefault(name,0) + 1);
            deptCodeMap.put(code, deptCodeMap.getOrDefault(code,0) + 1);

            // 获取计数结果，结果大于1时即为存在重复
            Integer nameCount = deptNameMap.get(name);
            Integer codeCount = deptCodeMap.get(code);
            if (nameCount > 1) {
                deptNameRepeatSet.add(name);
            }
            if (codeCount > 1) {
                deptCodeRepeatSet.add(code);
            }
        });
    }

    // 检查状态
    private void checkStatus(List<String> ids) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysDept::getStatus,"0")
                .in(SysDept::getId,ids);
        Long count = sysDeptMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("部门状态为正常不允许删除");
        }
    }

    // 验证是否存在子集
    private void checkChildren(List<String> ids) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(SysDept::getParentId,ids)
                .select(SysDept::getId);
        List<SysDept> sysDepts = sysDeptMapper.selectList(queryWrapper);

        if (sysDepts.isEmpty()) {
            return;
        }

        // 对比以删除节点为父节点的数据，当这些数据全部与删除的数据相同，则要删除的数据中没有子节点存在
        List<String> list = new java.util.ArrayList<>(sysDepts.stream().map(SysDept::getId).toList());
        list.removeAll(ids);

        if (!list.isEmpty()) {
            throw new ServiceException("存在子集不允许删除");
        }
    }

    // 验证是否存在岗位
    private void checkPost(List<String> ids) {
        Long count = sysPostService.queryCountByDeptId(ids);
        if (count > 0) {
            throw new ServiceException("存在岗位不允许删除");
        }
    }

    // 验证是否关联了用户
    private void checkUser(List<String> ids) {
        Long count = sysDeptMapper.deptUserCount(ids);
        if (count > 0) {
            throw new ServiceException("存在用户不允许删除");
        }
    }

    // 校验dept code唯一性
    private void checkDeptCode(SysDept sysDept) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDept::getCode,sysDept.getCode());
        List<SysDept> sysDeptList = sysDeptMapper.selectList(queryWrapper);

        if (sysDeptList.isEmpty()) {
            return;
        }

        if (sysDeptList.size() > 1) {
            throw new ServiceException("部门编码已存在");
        }

        if (!sysDeptList.get(0).getId().equals(sysDept.getId())) {
            throw new ServiceException("部门编码已存在");
        }
    }

    private void checkDeptName(SysDept sysDept) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDept::getName,sysDept.getName());
        List<SysDept> sysDeptList = sysDeptMapper.selectList(queryWrapper);

        if (sysDeptList.isEmpty()) {
            return;
        }

        if (sysDeptList.size() > 1) {
            throw new ServiceException("部门名称已存在");
        }

        if (!sysDeptList.get(0).getId().equals(sysDept.getId())) {
            throw new ServiceException("部门名称已存在");
        }
    }

    // 获取名称路径
    private String getNamePath(List<SysDeptVO> sysDeptList, String deptId) {
        List<SysDeptVO> targetSingleList = sysDeptList.stream().filter(dept -> dept.getId().equals(deptId)).toList();
        if (!targetSingleList.isEmpty()) {
            // 获取到最末级（自己）
            SysDeptVO sysDept = targetSingleList.get(0);
            String name = sysDept.getName();
            //  当前pid为 0 即为顶级部门，名称路径为自己部门名称
            if ("0".equals(sysDept.getParentId())) {
                return name;
            } else {
                String parentName = getNamePath(sysDeptList, sysDept.getParentId());
                return parentName + "/" + name;
            }
        }
        return null;
    }
}
