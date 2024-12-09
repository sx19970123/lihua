package com.lihua.system.strategy.impl.registeruser;

import com.lihua.system.entity.SysUserPost;
import com.lihua.system.model.dto.SysSettingDTO;
import com.lihua.system.service.SysUserPostService;
import com.lihua.system.strategy.SaveRegisterUserAssociatedStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SavePostStrategyImpl implements SaveRegisterUserAssociatedStrategy {

    @Resource
    private SysUserPostService sysUserPostService;

    @Override
    public void saveRegisterUserAssociated(String userId, SysSettingDTO.SignInSetting signInSetting) {
        // 用户岗位关联表
        List<String> postIds = signInSetting.getPostIds();
        if (!postIds.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            List<SysUserPost> sysUserPosts  = new ArrayList<>(postIds.size());
            postIds.forEach(postId -> sysUserPosts.add(new SysUserPost(userId, postId, now, null)));
            sysUserPostService.save(sysUserPosts);
        }
    }
}
