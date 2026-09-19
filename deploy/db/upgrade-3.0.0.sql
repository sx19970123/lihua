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
