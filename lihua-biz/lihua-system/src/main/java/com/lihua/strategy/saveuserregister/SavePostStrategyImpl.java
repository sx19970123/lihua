package com.lihua.strategy.saveuserregister;

import com.lihua.common.utils.date.DateUtils;
import com.lihua.entity.SysUserPost;
import com.lihua.model.dto.SysSettingDTO;
import com.lihua.service.SysPostService;
import com.lihua.service.SysUserPostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SavePostStrategyImpl implements SaveRegisterUserAssociatedStrategy {

    @Resource
    private SysPostService sysPostService;

    @Resource
    private SysUserPostService sysUserPostService;

    @Override
    public void saveRegisterUserAssociated(String userId, SysSettingDTO.SignUpSetting signUpSetting) {
        // 用户岗位关联表（过滤已删除的岗位——删除操作不清理注册配置，JSON 可能残留悬空引用；空配置或全失效则不写入）
        List<String> validPostIds = sysPostService.queryExistIds(signUpSetting.getPostIds());
        if (validPostIds.isEmpty()) {
            return;
        }

        LocalDateTime now = DateUtils.now();
        List<SysUserPost> sysUserPosts = new ArrayList<>(validPostIds.size());
        validPostIds.forEach(postId -> sysUserPosts.add(new SysUserPost(userId, postId, now, null)));
        sysUserPostService.save(sysUserPosts);
    }
}
