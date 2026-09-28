-- ----------------------------------------------------------------------------
-- 狸花猫 2.2.0 → 3.0.0 升级脚本（幂等，可重复执行）
-- 内容：字典管理新增「业务域」字段 + sys_dict_business_domain 字典种子 + 存量字典归类
--      附件表 is_public 行级公开标记 + 存量回填 + 热路径索引（4.9 附件域 S4/A5）
--      「系统组件」目录补「密码输入」演示页菜单种子（回归 WEB-083，动态路由缺种子致 404）
-- 全新安装：先导入 lihua.sql（2.2.0 基线），再执行本脚本
-- 执行完成后：请在「系统管理-字典管理」页点击「刷新缓存」
-- ----------------------------------------------------------------------------

-- 1. sys_dict_type 加业务域列（MySQL 无 ADD COLUMN IF NOT EXISTS，经 information_schema 判断）
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_dict_type' AND COLUMN_NAME = 'business_domain'
);
SET @ddl = IF(@col_exists = 0,
    'ALTER TABLE `sys_dict_type` ADD COLUMN `business_domain` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT ''业务域'' AFTER `type`',
    'SELECT ''column business_domain already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. 业务域字典类型（存在性按 code 判断）
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`)
SELECT 1946800000000000001, '业务域', 'sys_dict_business_domain', '0', 'dict', '字典所属业务域分类', 1, NOW(), NULL, NULL, '0', '0'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` WHERE `code` = 'sys_dict_business_domain');

-- 3. 业务域选项（存在性按 dict_type_code + value 逐条判断）
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT * FROM (
    SELECT 1946800000000000002 AS id, 0 AS parent_id, 'sys_dict_business_domain' AS dict_type_code, '通用' AS label, 'common' AS `value`, 1 AS sort, NULL AS remark, '0' AS del_flag, 1 AS create_id, NOW() AS create_time, NULL AS update_id, NULL AS update_time, '0' AS `status`, 'processing' AS tag_style
    UNION ALL SELECT 1946800000000000003, 0, 'sys_dict_business_domain', '字典管理', 'dict', 2, NULL, '0', 1, NOW(), NULL, NULL, '0', 'default'
    UNION ALL SELECT 1946800000000000004, 0, 'sys_dict_business_domain', '菜单', 'menu', 3, NULL, '0', 1, NOW(), NULL, NULL, '0', 'warning'
    UNION ALL SELECT 1946800000000000005, 0, 'sys_dict_business_domain', '用户组织', 'user', 4, NULL, '0', 1, NOW(), NULL, NULL, '0', 'success'
    UNION ALL SELECT 1946800000000000006, 0, 'sys_dict_business_domain', '通知公告', 'notice', 5, NULL, '0', 1, NOW(), NULL, NULL, '0', 'processing'
    UNION ALL SELECT 1946800000000000007, 0, 'sys_dict_business_domain', '附件', 'attachment', 6, NULL, '0', 1, NOW(), NULL, NULL, '0', 'default'
    UNION ALL SELECT 1946800000000000008, 0, 'sys_dict_business_domain', '监控日志', 'monitor', 7, NULL, '0', 1, NOW(), NULL, NULL, '0', 'warning'
    UNION ALL SELECT 1946800000000000009, 0, 'sys_dict_business_domain', '测试', 'test', 8, NULL, '0', 1, NOW(), NULL, NULL, '0', 'default'
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'sys_dict_business_domain' AND `value` = seed.`value`
);

-- 4. 存量字典归类（UPDATE 天然幂等）
UPDATE `sys_dict_type` SET `business_domain` = 'common'     WHERE `code` IN ('sys_status', 'sys_whether');
UPDATE `sys_dict_type` SET `business_domain` = 'dict'       WHERE `code` IN ('sys_dict_type', 'sys_dict_tag_style', 'sys_dict_business_domain');
UPDATE `sys_dict_type` SET `business_domain` = 'menu'       WHERE `code` IN ('sys_menu_type', 'sys_link_menu_open_type');
UPDATE `sys_dict_type` SET `business_domain` = 'user'       WHERE `code` IN ('user_gender', 'sys_user_register_type', 'sys_client_type', 'sys_dept_type');
UPDATE `sys_dict_type` SET `business_domain` = 'notice'     WHERE `code` IN ('sys_notice_status', 'sys_notice_type', 'sys_notice_user_scope', 'sys_notice_priority');
UPDATE `sys_dict_type` SET `business_domain` = 'attachment' WHERE `code` IN ('sys_attachment_status', 'sys_attachment_upload_mode');
UPDATE `sys_dict_type` SET `business_domain` = 'monitor'    WHERE `code` IN ('sys_log_status');
UPDATE `sys_dict_type` SET `business_domain` = 'test'       WHERE `code` IN ('test_tree');

-- ----------------------------------------------------------------------------
-- 4.9 附件域（is_public 行级公开标记 + 热路径索引）
-- 类型说明：is_public 为纯布尔无字典值域，取 tinyint(1)（JDBC 原生映射 Boolean）；
-- 库内 char(1) 标记列（status/del_flag）均为字典语义，不适用布尔场景
-- ----------------------------------------------------------------------------

-- 5. sys_attachment 加行级公开标记列（MySQL 无 ADD COLUMN IF NOT EXISTS，经 information_schema 判断）
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_attachment' AND COLUMN_NAME = 'is_public'
);
SET @ddl = IF(@col_exists = 0,
    'ALTER TABLE `sys_attachment` ADD COLUMN `is_public` tinyint(1) NOT NULL DEFAULT 0 COMMENT ''是否公开访问（0=私密 1=公开，上传时物化，不可变）'' AFTER `client_type`',
    'SELECT ''column is_public already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 6. 存量回填：旧公开上传白名单三码（公开内容内生场景）→ 公开，其余保持私密（UPDATE 幂等）
UPDATE `sys_attachment` SET `is_public` = 1
WHERE `business_code` IN ('UserAvatar', 'SystemNotice', 'EditorIndex') AND `is_public` = 0;

-- 7. 热路径索引 ×4（此前除主键外零二级索引；MySQL 无 CREATE INDEX IF NOT EXISTS，经 information_schema 判断）
SET @idx_exists = (
    SELECT COUNT(*) FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_attachment' AND INDEX_NAME = 'idx_sys_attachment_md5'
);
SET @ddl = IF(@idx_exists = 0, 'CREATE INDEX `idx_sys_attachment_md5` ON `sys_attachment` (`md5`)', 'SELECT ''index idx_sys_attachment_md5 already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists = (
    SELECT COUNT(*) FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_attachment' AND INDEX_NAME = 'idx_sys_attachment_upload_id'
);
SET @ddl = IF(@idx_exists = 0, 'CREATE INDEX `idx_sys_attachment_upload_id` ON `sys_attachment` (`upload_id`)', 'SELECT ''index idx_sys_attachment_upload_id already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists = (
    SELECT COUNT(*) FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_attachment' AND INDEX_NAME = 'idx_sys_attachment_create_time'
);
SET @ddl = IF(@idx_exists = 0, 'CREATE INDEX `idx_sys_attachment_create_time` ON `sys_attachment` (`create_time`)', 'SELECT ''index idx_sys_attachment_create_time already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_exists = (
    SELECT COUNT(*) FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_attachment' AND INDEX_NAME = 'idx_sys_attachment_path'
);
SET @ddl = IF(@idx_exists = 0, 'CREATE INDEX `idx_sys_attachment_path` ON `sys_attachment` (`path`)', 'SELECT ''index idx_sys_attachment_path already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 8. 通知公告正文存量公开链迁移（/p 取消；REPLACE 天然幂等，无匹配行零影响；代理前缀 /dev-api /prod-api 保留）
UPDATE `sys_notice`
SET `content` = REPLACE(`content`, 'storage/download/p?fullPath=', 'storage/download?fullPath=')
WHERE `content` LIKE '%storage/download/p?fullPath=%';

-- 9. 附件域死代码清理（4.9 T3.8 B4）：url 列全库零读写（URL上传功能无后端实现，历史列恒 NULL）；
--    「URL上传」字典值（sys_attachment_upload_mode value=3）无任何代码写入，一并清理
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_attachment' AND COLUMN_NAME = 'url'
);
SET @ddl = IF(@col_exists = 1,
    'ALTER TABLE `sys_attachment` DROP COLUMN `url`',
    'SELECT ''column url not exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

DELETE FROM `sys_dict_data` WHERE `dict_type_code` = 'sys_attachment_upload_mode' AND `value` = '3';

-- 10. 「数据监控」（druid）菜单退役（web 体检 W-C5：后端无 druid 依赖、/druid/** 放行已删，监控页为死页一并下线；
--     DELETE 天然幂等；菜单为 web 动态菜单，App 不消费）
DELETE FROM `sys_menu` WHERE `id` = 1838036487672111105 AND `component_path` = '/monitor/druid/MonitorDruid.vue';

-- 11. 两日志表加链路追踪 id 列（cloud 体检 D9：traceId 落库，页面记录可按此串联日志文件完整调用链；
--     存量记录空值正常，新记录起有值；无索引——按 trace_id 检索为低频排障操作）
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_operate_log' AND COLUMN_NAME = 'trace_id'
);
SET @ddl = IF(@col_exists = 0,
    'ALTER TABLE `sys_operate_log` ADD COLUMN `trace_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT ''链路追踪id（入口生成，日志文件按此串联）'' AFTER `url`',
    'SELECT ''column trace_id already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_login_log' AND COLUMN_NAME = 'trace_id'
);
SET @ddl = IF(@col_exists = 0,
    'ALTER TABLE `sys_login_log` ADD COLUMN `trace_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT ''链路追踪id（入口生成，日志文件按此串联）'' AFTER `url`',
    'SELECT ''column trace_id already exists, skip'' AS msg');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 12. 「系统组件」目录补「密码输入」演示页菜单（3.0 回归 WEB-083：password-input 前端演示页文件齐全，
--     菜单种子缺记录致 web 动态路由未注册 404；与 lihua.sql 基线同一条记录；存在性按 router_path 判断；
--     菜单 App 不消费，无 App 联动）
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`)
SELECT 2101309446872371201, '1866101773239513090', '密码输入', '密码输入', 'page', '/password-input', '/component/password-input/PasswordInputIndex.vue', '0', '0', 'page', 'LockOutlined', 14, 1, NOW(), NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `router_path` = '/password-input' AND `del_flag` = '0');

-- 13. 客户端类型字典补 H5 项（App H5 适配 H-4：getClientType() 补 H5 分支发送 'app_h5'，
--     在线用户/登录日志展示需要字典项；与 lihua.sql 基线同一条记录；存在性按 value 判断；
--     双仓共库一次生效）
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2101975000000000001, 0, 'sys_client_type', 'APP-H5', 'app_h5', 4, NULL, '0', 1, NOW(), NULL, NULL, '0', 'warning'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'sys_client_type' AND `value` = 'app_h5');

-- ----------------------------------------------------------------------------
-- App 版本管理（lihua-ani 移植：sys_app_version 表 + 字典 + web 菜单种子；
-- App 检查更新接口 /app/system/app-version/check 匿名放行随 SecurityConfig 代码；
-- download_url 存附件 path（android）或绝对外链（ios），端上按需补全；双仓共库一次生效）
-- ----------------------------------------------------------------------------

-- 14. 建表（全新安装由 lihua.sql 基线建立，存量库由此补齐；幂等）
CREATE TABLE IF NOT EXISTS `sys_app_version` (
  `id` bigint NOT NULL COMMENT '主键id（雪花）',
  `version_name` varchar(20) NOT NULL COMMENT '版本名称（与 manifest.json versionName 一致，如 1.2.0）',
  `version_code` int NOT NULL COMMENT '版本序号（与 manifest.json versionCode 一致，整数，如 10200）',
  `platform` varchar(10) NOT NULL COMMENT '平台（字典 app_version_platform：android/ios）',
  `download_url` varchar(500) NOT NULL COMMENT '主包地址（android=apk附件path或HTTP(S)直链；ios=外部跳转链接）',
  `enable_wgt` char(1) NOT NULL DEFAULT '0' COMMENT '是否支持 wgt 热更新（0 否 / 1 是，ios 恒为 0）',
  `wgt_download_url` varchar(500) DEFAULT NULL COMMENT '热更新地址（仅 android，enable_wgt=1 时必填附件path或HTTP(S)直链）',
  `update_content` text COMMENT '更新说明（纯文本多行）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（字典 app_version_status：0草稿/1已发布/2已下线）',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0 存在 / 1 删除）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_platform_status` (`platform`,`status`) USING BTREE,
  KEY `idx_version_code` (`version_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='App版本发布记录';

-- 15. 业务域字典补「App版本」选项（存在性按 value 判断）
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000303, 0, 'sys_dict_business_domain', 'App版本', 'app', 9, NULL, '0', 1, NOW(), NULL, NULL, '0', 'processing'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'sys_dict_business_domain' AND `value` = 'app');

-- 16. App 版本字典类型 ×2（存在性按 code 判断）
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`)
SELECT 2026092400000000301, 'App版本平台', 'app_version_platform', '0', 'app', 'App版本发布适用的平台', 1, NOW(), NULL, NULL, '0', '0'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` WHERE `code` = 'app_version_platform');

INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`)
SELECT 2026092400000000302, 'App版本状态', 'app_version_status', '0', 'app', 'App版本发布记录的状态', 1, NOW(), NULL, NULL, '0', '0'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` WHERE `code` = 'app_version_status');

-- 17. App 版本字典数据 ×5（存在性按 dict_type_code + value 逐条判断）
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000304, 0, 'app_version_platform', 'Android', 'android', 1, NULL, '0', 1, NOW(), NULL, NULL, '0', 'success'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_platform' AND `value` = 'android');

INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000305, 0, 'app_version_platform', 'iOS', 'ios', 2, NULL, '0', 1, NOW(), NULL, NULL, '0', 'processing'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_platform' AND `value` = 'ios');

INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000306, 0, 'app_version_status', '草稿', '0', 1, NULL, '0', 1, NOW(), NULL, NULL, '0', 'default'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_status' AND `value` = '0');

INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000307, 0, 'app_version_status', '已发布', '1', 2, NULL, '0', 1, NOW(), NULL, NULL, '0', 'success'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_status' AND `value` = '1');

INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000308, 0, 'app_version_status', '已下线', '2', 3, NULL, '0', 1, NOW(), NULL, NULL, '0', 'error'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_status' AND `value` = '2');

-- 18. web 菜单：App版本管理 page + 5 个按钮 perms（菜单 App 不消费；存在性按 router_path / id 判断）
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`)
SELECT 2026092400000000101, '1775035631645659138', 'App版本管理', 'App版本管理', 'page', '/app-version', '/system/app-version/SystemAppVersion.vue', '0', '0', 'page', 'MobileOutlined', 9, 1, NOW(), NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `router_path` = '/app-version' AND `del_flag` = '0');

INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`)
SELECT 2026092400000000102, '2026092400000000101', '版本新增', '版本新增', 'perms', NULL, NULL, '0', '0', 'system:appVersion:create', NULL, 1, 1, NOW(), NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `id` = 2026092400000000102);

INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`)
SELECT 2026092400000000103, '2026092400000000101', '版本编辑', '版本编辑', 'perms', NULL, NULL, '0', '0', 'system:appVersion:update', NULL, 2, 1, NOW(), NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `id` = 2026092400000000103);

INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`)
SELECT 2026092400000000104, '2026092400000000101', '版本删除', '版本删除', 'perms', NULL, NULL, '0', '0', 'system:appVersion:delete', NULL, 3, 1, NOW(), NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `id` = 2026092400000000104);

INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`)
SELECT 2026092400000000105, '2026092400000000101', '版本发布', '版本发布', 'perms', NULL, NULL, '0', '0', 'system:appVersion:publish', NULL, 4, 1, NOW(), NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `id` = 2026092400000000105);

INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`)
SELECT 2026092400000000106, '2026092400000000101', '版本下线', '版本下线', 'perms', NULL, NULL, '0', '0', 'system:appVersion:offline', NULL, 5, 1, NOW(), NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `id` = 2026092400000000106);

-- 19. App 热更新开关字典（enable_wgt 与 sys_whether 值集相同语义不同，独立建字典；配套枚举
--     AppVersionEnableWgtEnum；存在性按 code / dict_type_code + value 判断）
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`)
SELECT 2026092400000000309, 'App热更新支持', 'app_version_enable_wgt', '0', 'app', 'App版本是否支持wgt热更新（与sys_whether值集相同语义不同，独立字典）', 1, NOW(), NULL, NULL, '0', '0'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` WHERE `code` = 'app_version_enable_wgt');

INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000310, 0, 'app_version_enable_wgt', '否', '0', 1, NULL, '0', 1, NOW(), NULL, NULL, '0', 'default'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_enable_wgt' AND `value` = '0');

INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000311, 0, 'app_version_enable_wgt', '是', '1', 2, NULL, '0', 1, NOW(), NULL, NULL, '0', 'success'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_enable_wgt' AND `value` = '1');

-- 20. App 版本平台字典补鸿蒙项（配套枚举 AppPlatformEnum.HARMONYOS，value 取 uni-app 平台标识；
--     鸿蒙与 iOS 同走外链跳转通道；存在性按 value 判断）
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`)
SELECT 2026092400000000312, 0, 'app_version_platform', 'HarmonyOS', 'harmony', 3, NULL, '0', 1, NOW(), NULL, NULL, '0', 'warning'
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_data` WHERE `dict_type_code` = 'app_version_platform' AND `value` = 'harmony');
