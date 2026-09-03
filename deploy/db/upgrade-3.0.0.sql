-- ----------------------------------------------------------------------------
-- 狸花猫 2.2.0 → 3.0.0 升级脚本（幂等，可重复执行）
-- 内容：字典管理新增「业务域」字段 + sys_dict_business_domain 字典种子 + 存量字典归类
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
