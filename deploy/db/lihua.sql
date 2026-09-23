/*
 Navicat Premium Dump SQL

 Source Server         : MySQL8
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : lihua

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 24/09/2026 00:18:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment` (
  `id` bigint NOT NULL COMMENT '主键',
  `storage_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件数据库名',
  `original_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件原名称',
  `extension_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件扩展名',
  `path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件保存路径',
  `upload_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分片上传id',
  `business_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务编码',
  `business_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务名称',
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件大小',
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件类型',
  `upload_mode` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上传方式（一般上传、分片上传、文件秒传）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上传状态（成功、失败、分片上传中、业务删除）',
  `storage_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件存储位置',
  `md5` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件md5值',
  `create_id` bigint DEFAULT NULL COMMENT '上传人id',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `update_id` bigint DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '删除标识',
  `error_msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上传失败原因',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '原url（通过url上传有该字段）',
  `client_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '客户端类型',
  `is_public` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否公开访问（0=私密 1=公开，上传时物化，不可变）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_sys_attachment_md5` (`md5`),
  KEY `idx_sys_attachment_upload_id` (`upload_id`),
  KEY `idx_sys_attachment_create_time` (`create_time`),
  KEY `idx_sys_attachment_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统附件表';

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL COMMENT ' 主键',
  `parent_id` bigint DEFAULT NULL COMMENT '父级id',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '编码',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `manager` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '负责人',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `fax` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '传真',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统单位/岗位表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`id`, `parent_id`, `name`, `code`, `sort`, `status`, `manager`, `phone_number`, `email`, `fax`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES (1810226204790657025, 0, '狸花猫科技', 'lihua', 1, '0', '', '', '', NULL, '0', 1, '2024-07-08 16:15:34', 1, '2026-09-06 11:54:56', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父级字典id',
  `dict_type_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型编码',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典标签',
  `value` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典值',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `tag_style` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签的样式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771163317281083393, 0, 'sys_status', '正常', '0', 1, NULL, '0', 1, '2024-03-22 21:13:36', 1, '2024-03-24 13:42:09', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771163394133315585, 0, 'sys_status', '停用', '1', 2, NULL, '0', 1, '2024-03-22 21:13:54', 1, '2024-03-22 21:18:01', '0', 'error');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771165766595235841, 0, 'sys_dict_type', '一般字典', '0', 1, NULL, '0', 1, '2024-03-22 21:23:20', 1, '2024-03-24 20:46:11', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771165768948240385, 0, 'sys_dict_type', '树型字典', '1', 2, NULL, '0', 1, '2024-03-22 21:23:21', 1, '2024-03-24 20:46:12', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771166154488664065, 0, 'sys_dict_tag_style', '默认', 'default', 1, NULL, '0', 1, '2024-03-22 21:24:53', 1, '2024-07-05 15:25:08', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771166156803919874, 0, 'sys_dict_tag_style', '主要', 'processing', 2, NULL, '0', 1, '2024-03-22 21:24:53', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771166159454720001, 0, 'sys_dict_tag_style', '成功', 'success', 3, NULL, '0', 1, '2024-03-22 21:24:54', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771166162894049282, 0, 'sys_dict_tag_style', '警告', 'warning', 4, NULL, '0', 1, '2024-03-22 21:24:55', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1771166168740909057, 0, 'sys_dict_tag_style', '错误', 'error', 5, NULL, '0', 1, '2024-03-22 21:24:56', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1773701158649315330, 0, 'sys_menu_type', '目录', 'directory', 1, NULL, '0', 1, '2024-03-29 21:18:05', 1, '2024-03-30 22:40:29', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1773701160050212865, 0, 'sys_menu_type', '页面', 'page', 2, NULL, '0', 1, '2024-03-29 21:18:05', 1, '2024-04-05 11:07:54', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1773701161455304706, 0, 'sys_menu_type', '权限', 'perms', 4, NULL, '0', 1, '2024-03-29 21:18:05', 1, '2024-04-07 20:17:27', '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1774046926568538113, 0, 'sys_menu_type', '链接', 'link', 3, NULL, '0', 1, '2024-03-30 20:12:02', 1, '2024-04-05 11:08:03', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1774250426447544321, 0, 'sys_link_menu_open_type', '系统内', 'inner', 1, NULL, '0', 1, '2024-03-31 09:40:40', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1774250428016214017, 0, 'sys_link_menu_open_type', '新页面', 'new-page', 2, NULL, '0', 1, '2024-03-31 09:40:41', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1774252801971306497, 0, 'sys_whether', '是', '0', 1, NULL, '0', 1, '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1774252803737108481, 0, 'sys_whether', '否', '1', 2, NULL, '0', 1, '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1780864938674667522, 0, 'sys_dept_type', '部门', 'dept', 1, NULL, '0', 1, '2024-04-18 15:44:23', 1, '2024-04-20 21:35:37', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1780864940843122690, 0, 'sys_dept_type', '岗位', 'post', 2, NULL, '0', 1, '2024-04-18 15:44:23', 1, '2024-04-20 21:35:37', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1794263233540739074, 0, 'user_gender', '女', '0', 2, NULL, '0', 1, '2024-05-25 15:04:25', 1, '2024-05-25 15:08:41', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1794263235541422081, 0, 'user_gender', '男', '1', 1, NULL, '0', 1, '2024-05-25 15:04:26', 1, '2024-05-25 15:08:41', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1794263238364188674, 0, 'user_gender', '不愿透露', '2', 3, NULL, '0', 1, '2024-05-25 15:04:26', 1, '2024-09-25 19:22:13', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814191392971739137, 0, 'sys_notice_status', '未发布', '0', 1, NULL, '0', 1, '2024-07-19 14:51:49', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814191395400241153, 0, 'sys_notice_status', '已发布', '1', 2, NULL, '0', 1, '2024-07-19 14:51:49', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814191397560307713, 0, 'sys_notice_status', '已撤销', '2', 3, NULL, '0', 1, '2024-07-19 14:51:50', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814191691274833922, 0, 'sys_notice_type', '通知', '0', 1, NULL, '0', 1, '2024-07-19 14:53:00', 1, '2026-09-23 01:34:47', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814191692868669441, 0, 'sys_notice_type', '公告', '1', 2, NULL, '0', 1, '2024-07-19 14:53:00', 1, '2024-07-20 17:53:15', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814602689115856897, 0, 'sys_notice_user_scope', '全部用户', '0', 1, NULL, '0', 1, '2024-07-20 18:06:09', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814602691913457666, 0, 'sys_notice_user_scope', '指定用户', '1', 2, NULL, '0', 1, '2024-07-20 18:06:10', 1, '2024-07-21 22:04:21', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814603526915497986, 0, 'sys_notice_priority', '紧急', '0', 1, NULL, '0', 1, '2024-07-20 18:09:29', 1, '2024-07-21 20:59:43', '0', 'red');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814603528811323393, 0, 'sys_notice_priority', '高', '1', 2, NULL, '0', 1, '2024-07-20 18:09:30', 1, '2024-07-21 20:59:44', '0', 'orange');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814603531445346306, 0, 'sys_notice_priority', '中', '2', 3, NULL, '0', 1, '2024-07-20 18:09:30', 1, '2024-07-21 20:59:44', '0', 'blue');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1814603537514504193, 0, 'sys_notice_priority', '低', '3', 4, NULL, '0', 1, '2024-07-20 18:09:32', 1, '2024-07-21 20:59:45', '0', 'green');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1823522986106695682, 0, 'sys_log_status', '成功', '0', 1, NULL, '0', 1, '2024-08-14 08:52:14', 1, '2024-08-14 08:52:43', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1823522988254179330, 0, 'sys_log_status', '失败', '1', 2, NULL, '0', 1, '2024-08-14 08:52:14', 1, '2024-08-14 08:52:43', '0', 'error');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1840380919319064578, 0, 'sys_user_register_type', '管理员注册', '0', 1, NULL, '0', 1, '2024-09-29 21:19:38', 1, '2025-03-14 17:58:25', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1840380921978253313, 0, 'sys_user_register_type', '用户自助注册', '1', 2, NULL, '0', 1, '2024-09-29 21:19:39', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1869716623098253313, 0, 'tree', '节点1', '1', 1, NULL, '0', 1, '2024-12-19 20:09:15', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1869716680958676994, 1869716623098253313, 'tree', '节点1-1', '1-1', 1, NULL, '0', 1, '2024-12-19 20:09:29', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1869716725632208898, 1869716623098253313, 'tree', '节点1-2', '1-2', 2, NULL, '0', 1, '2024-12-19 20:09:40', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1869716755554373633, 0, 'tree', '节点2', '2', 2, NULL, '0', 1, '2024-12-19 20:09:47', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1869716801964347394, 1869716755554373633, 'tree', '节点2-1', '2-1', 1, NULL, '0', 1, '2024-12-19 20:09:58', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1869716862974693377, 1869716755554373633, 'tree', '节点2-2', '2-2', 2, NULL, '0', 1, '2024-12-19 20:10:12', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1869719177509081090, 1869716862974693377, 'tree', '节点2-2-1', '2-2-1', 1, NULL, '0', 1, '2024-12-19 20:19:24', 1, '2026-09-23 22:28:49', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1880166202906210305, 0, 'sys_user_register_type', '批量导入注册', '2', 3, NULL, '0', 1, '2025-01-17 16:12:09', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896495786205185, 0, 'sys_attachment_upload_mode', '一般上传', '0', 1, NULL, '0', 1, '2025-02-21 19:17:48', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896497644281857, 0, 'sys_attachment_upload_mode', '分片上传', '1', 2, NULL, '0', 1, '2025-02-21 19:17:48', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896500349607937, 0, 'sys_attachment_upload_mode', '文件秒传', '2', 3, NULL, '0', 1, '2025-02-21 19:17:49', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896502400622593, 0, 'sys_attachment_upload_mode', 'URL上传', '3', 4, NULL, '0', 1, '2025-02-21 19:17:49', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896965527281665, 0, 'sys_attachment_status', '上传成功', '0', 1, NULL, '0', 1, '2025-02-21 19:19:40', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896966877847554, 0, 'sys_attachment_status', '上传失败', '1', 2, NULL, '0', 1, '2025-02-21 19:19:40', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896969004359681, 0, 'sys_attachment_status', '分片上传中', '2', 3, NULL, '0', 1, '2025-02-21 19:19:40', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1892896971588050945, 0, 'sys_attachment_status', '业务删除', '3', 4, NULL, '0', 1, '2025-02-21 19:19:41', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1946800000000000002, 0, 'sys_dict_business_domain', '通用', 'common', 1, NULL, '0', 1, '2026-09-03 03:05:19', 1, '2026-09-03 11:25:01', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1946800000000000003, 0, 'sys_dict_business_domain', '字典管理', 'dict', 2, NULL, '0', 1, '2026-09-03 03:05:19', 1, '2026-09-03 11:25:02', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1946800000000000004, 0, 'sys_dict_business_domain', '菜单', 'menu', 3, NULL, '0', 1, '2026-09-03 03:05:19', 1, '2026-09-03 11:25:02', '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1946800000000000005, 0, 'sys_dict_business_domain', '用户组织', 'user', 4, NULL, '0', 1, '2026-09-03 03:05:19', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1946800000000000006, 0, 'sys_dict_business_domain', '通知公告', 'notice', 5, NULL, '0', 1, '2026-09-03 03:05:19', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1946800000000000007, 0, 'sys_dict_business_domain', '附件', 'attachment', 6, NULL, '0', 1, '2026-09-03 03:05:19', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1946800000000000008, 0, 'sys_dict_business_domain', '监控日志', 'monitor', 7, NULL, '0', 1, '2026-09-03 03:05:19', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1995424806233780225, 0, 'sys_client_type', 'WEB', 'web', 1, NULL, '0', 1, '2025-12-01 17:28:42', 1, '2026-04-29 08:34:32', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1995424807697592322, 0, 'sys_client_type', 'APP', 'app', 2, NULL, '0', 1, '2025-12-01 17:28:42', 1, '2026-09-03 10:33:18', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (1995424809337565185, 0, 'sys_client_type', '微信小程序', 'wechat_mp', 3, NULL, '0', 1, '2025-12-01 17:28:42', 1, '2026-09-22 08:45:52', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2095393706038095873, 0, 'tree', '节点3', '3', 3, NULL, '0', 1, '2026-09-03 14:09:25', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2095393801735335938, 2095393706038095873, 'tree', '节点3-1', '3-1', 1, NULL, '0', 1, '2026-09-03 14:09:48', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2095393801886330881, 2095393706038095873, 'tree', '节点3-2', '3-2', 2, NULL, '0', 1, '2026-09-03 14:09:48', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2095393802024742914, 2095393706038095873, 'tree', '节点3-3', '3-3', 3, NULL, '0', 1, '2026-09-03 14:09:48', 1, '2026-09-23 22:28:49', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2101975000000000001, 0, 'sys_client_type', 'APP-H5', 'app_h5', 4, NULL, '0', 1, '2026-09-21 10:03:50', 1, '2026-09-22 08:45:52', '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687082621706242, 0, 'sys_dict_tag_style', '洋红', 'magenta', 6, NULL, '0', 1, '2026-09-23 17:10:41', NULL, NULL, '0', 'magenta');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687082839810049, 0, 'sys_dict_tag_style', '红', 'red', 7, NULL, '0', 1, '2026-09-23 17:10:41', NULL, NULL, '0', 'red');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687083015970818, 0, 'sys_dict_tag_style', '火山', 'volcano', 8, '无法映射为css颜色', '0', 1, '2026-09-23 17:10:41', 1, '2026-09-23 17:16:03', '0', 'volcano');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687083192131586, 0, 'sys_dict_tag_style', '橙', 'orange', 9, NULL, '0', 1, '2026-09-23 17:10:41', NULL, NULL, '0', 'orange');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687083364098050, 0, 'sys_dict_tag_style', '金', 'gold', 10, NULL, '0', 1, '2026-09-23 17:10:41', NULL, NULL, '0', 'gold');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687083640922113, 0, 'sys_dict_tag_style', '青柠', 'lime', 11, NULL, '0', 1, '2026-09-23 17:10:41', NULL, NULL, '0', 'lime');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687083863220225, 0, 'sys_dict_tag_style', '绿', 'green', 12, NULL, '0', 1, '2026-09-23 17:10:41', NULL, NULL, '0', 'green');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687084089712642, 0, 'sys_dict_tag_style', '青', 'cyan', 13, NULL, '0', 1, '2026-09-23 17:10:42', NULL, NULL, '0', 'cyan');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687084240707585, 0, 'sys_dict_tag_style', '蓝', 'blue', 14, NULL, '0', 1, '2026-09-23 17:10:42', NULL, NULL, '0', 'blue');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687084500754434, 0, 'sys_dict_tag_style', '极客蓝', 'geekblue', 15, '无法映射为css颜色', '0', 1, '2026-09-23 17:10:42', 1, '2026-09-23 17:16:08', '0', 'geekblue');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES (2102687084693692417, 0, 'sys_dict_tag_style', '紫', 'purple', 16, NULL, '0', 1, '2026-09-23 17:10:42', NULL, NULL, '0', 'purple');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型名称',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型编码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型',
  `business_domain` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务域',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典备注',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1771163166122561537, '系统状态', 'sys_status', '0', 'common', '系统通用状态标识', 1, '2024-03-22 21:13:00', 1, '2024-07-19 15:27:12', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1771164641267666946, '字典标签样式', 'sys_dict_tag_style', '0', 'dict', '字典配置配置中，样式列的字典', 1, '2024-03-22 21:18:52', 1, '2024-03-22 21:20:00', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1771165529122131969, '字典类型', 'sys_dict_type', '0', 'dict', '区分字典为一般字典还是树型字典', 1, '2024-03-22 21:22:23', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1773700957867982850, '菜单类型', 'sys_menu_type', '0', 'menu', '系统菜单配置类型，分为 目录、页面、链接、权限', 1, '2024-03-29 21:17:17', 1, '2024-04-01 09:59:35', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1774249964684034050, '链接菜单打开方式', 'sys_link_menu_open_type', '0', 'menu', '链接菜单打开方式，分为系统内嵌套和浏览器新页面', 1, '2024-03-31 09:38:50', 1, '2024-03-31 09:41:45', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1774252683993923586, '系统是否', 'sys_whether', '0', 'common', '系统是否选项字典', 1, '2024-03-31 09:49:39', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1780864852875984898, '部门类型', 'sys_dept_type', '0', 'user', '保存部门操作时的类型选项，分为部门和岗位', 1, '2024-04-18 15:44:02', 1, '2024-04-20 21:35:36', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1794262937292853250, '用户性别', 'user_gender', '0', 'user', '系统用户性别字典', 1, '2024-05-25 15:03:15', 1, '2024-05-25 15:03:26', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1814191109734584322, '公告状态', 'sys_notice_status', '0', 'notice', '系统公告状态字典', 1, '2024-07-19 14:50:41', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1814191516905033729, '公告类型', 'sys_notice_type', '0', 'notice', '系统公告类型字典', 1, '2024-07-19 14:52:18', 1, '2024-07-19 14:52:32', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1814602561218945026, '用户范围', 'sys_notice_user_scope', '0', 'notice', '通知公告接收消息的用户范围', 1, '2024-07-20 18:05:39', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1814603011422953473, '优先级别', 'sys_notice_priority', '0', 'notice', '通知公告优先程度', 1, '2024-07-20 18:07:26', 1, '2024-07-29 22:13:21', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1823522921661214721, '日志执行结果', 'sys_log_status', '0', 'monitor', '日志记录程序执行是否成功', 1, '2024-08-14 08:51:59', 1, '2024-09-23 09:05:30', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1840380676900876290, '用户注册类型', 'sys_user_register_type', '0', 'user', '用户注册类型：管理员注册、用户自助注册、批量导入注册', 1, '2024-09-29 21:18:41', 1, '2025-01-17 16:12:24', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1869716543582638081, '树形字典演示', 'tree', '1', 'test', '测试用，可删除', 1, '2024-12-19 20:08:56', 1, '2026-09-23 22:28:49', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1892895886177673218, '附件状态', 'sys_attachment_status', '0', 'attachment', '系统附件上传状态', 1, '2025-02-21 19:15:22', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1892896049893941249, '上传方式', 'sys_attachment_upload_mode', '0', 'attachment', '系统附件上传方式', 1, '2025-02-21 19:16:01', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (1995424677359595521, '客户端类型', 'sys_client_type', '0', 'user', '用于区分web｜app｜小程序', 1, '2025-12-01 17:28:11', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `business_domain`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES (2095345643714088962, '业务域', 'sys_dict_business_domain', '0', 'dict', '在字典中指定业务域', 1, '2026-09-03 10:58:26', 1, '2026-09-17 16:04:23', '0', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` bigint NOT NULL COMMENT '主键',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务描述',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务类型编码',
  `type_msg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务类型描述',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类名',
  `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '方法名',
  `ip_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'ip地址',
  `create_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人昵称',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id（操作人）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间（操作时间）',
  `execute_time` int DEFAULT NULL COMMENT '执行时长ms',
  `params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '参数',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '返回值',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '异常信息',
  `error_stack` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '堆栈信息',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求url',
  `trace_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '链路追踪id（入口生成，日志文件按此串联）',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户代理字符串，包含客户端操作系统、浏览器内核等信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除',
  `execute_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '日志执行状态',
  `cache_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户缓存key',
  `client_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '客户端类型',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'ip归属地',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `cache_key` (`cache_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统登录日志';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父级菜单id',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单名称',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '鼠标悬浮展示内容',
  `menu_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单/页面/按钮/外链',
  `router_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由地址',
  `component_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组建路径',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否显示（0显示、1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单状态(0正常、1停用)',
  `perms` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识符',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单图标',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否缓存页面（0 缓存、1不缓存）',
  `link_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '外链类型页面地址',
  `query` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由携带的参数',
  `view_tab` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '允许view-tab显示标签',
  `link_open_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '链接打开方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1775035631645659138, '0', '系统管理', '系统管理', 'directory', '/system', NULL, '0', '0', 'directory', 'SettingOutlined', 1, 1, '2024-04-02 13:40:48', '1', '2025-11-06 22:04:58', '0', NULL, '1', NULL, NULL, '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1776948212783730690, '1897209872981360641', '字典编辑', '字典编辑', 'perms', '', NULL, '0', '0', 'system:dict:modify', NULL, 1, 1, '2024-04-07 20:20:43', '1', '2024-10-04 20:45:15', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1777536058626834433, '1775035631645659138', '角色管理', '角色管理', 'page', '/role', '/system/role/SystemRole.vue', '0', '0', 'page', 'TeamOutlined', 2, 1, '2024-04-09 11:16:36', '1', '2024-06-02 12:23:03', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1777536311941824513, '1775035631645659138', '用户管理', '用户管理', 'page', '/user', '/system/user/SystemUser.vue', '0', '0', 'page', 'UserSwitchOutlined', 1, 1, '2024-04-09 11:17:36', '1', '2024-06-02 12:22:57', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1777536895235293186, '1775035631645659138', '部门管理', '部门管理', 'page', '/dept', '/system/dept/SystemDept.vue', '0', '0', 'page', 'ApartmentOutlined', 4, 1, '2024-04-09 11:19:56', '1', '2024-06-02 12:23:50', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1777538040162844673, '0', '日志管理', '日志管理', 'directory', '/log', NULL, '0', '0', 'directory', 'FileSearchOutlined', 2, 1, '2024-04-09 11:24:28', '1', '2024-11-13 21:04:51', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1777538768721838082, '1777538040162844673', '登录日志', '登录日志', 'page', '/login', '/system/log/SystemLoginLog.vue', '0', '0', 'page', 'FileProtectOutlined', 1, 1, '2024-04-09 11:27:22', '1', '2024-11-13 21:04:51', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1777539832263114753, '1775035631645659138', '通知公告', '通知公告', 'page', '/system/notice', '/system/notice/SystemNotice.vue', '0', '0', 'page', 'MessageOutlined', 8, 1, '2024-04-09 11:31:36', '1', '2025-02-21 19:27:12', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1784084466574819330, '1775035631645659138', '岗位管理', '岗位管理', 'page', '/post', '/system/post/SystemPost.vue', '0', '0', 'page', 'ScheduleOutlined', 5, 1, '2024-04-27 12:57:38', '1', '2024-06-16 18:46:09', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1823518320191033346, '1777538040162844673', '操作日志', '操作日志', 'page', '/operate', '/system/log/SystemOperateLog.vue', '0', '0', 'page', 'FileSyncOutlined', 2, 1, '2024-08-14 08:33:42', '1', '2024-11-13 21:04:52', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1838036266821033985, '0', '系统监控', '系统监控', 'directory', '/monitor', NULL, '0', '0', 'directory', 'FundViewOutlined', 3, 1, '2024-09-23 10:02:50', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1838487275271753729, '1838036266821033985', '在线用户', '在线用户', 'page', '/loggedUser', '/monitor/logged-user/MonitorLoggedUser.vue', '0', '0', 'page', 'UserOutlined', 2, 1, '2024-09-24 15:54:59', '1', '2024-09-24 18:23:11', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1838581685913554946, '1838036266821033985', '服务监控', '服务监控', 'page', '/server', '/monitor/server/MonitorServer.vue', '0', '0', 'page', 'CloudServerOutlined', 4, 1, '2024-09-24 22:10:08', '1', '2024-10-02 13:34:22', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1840684744634474498, '1838036266821033985', '缓存监控', '缓存监控', 'page', '/cache', '/monitor/cache/MonitorCache.vue', '0', '0', 'page', 'CodepenOutlined', 3, 1, '2024-09-30 17:26:56', '1', '2024-10-02 13:34:18', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1841350424845307905, '1838036266821033985', '定时任务', '定时任务', 'link', '/job', NULL, '0', '0', 'link', 'FieldTimeOutlined', 1, 1, '2024-10-02 13:32:07', '1', '2024-10-02 13:39:28', '0', '定时任务基于 snail job。 使用时请启动【任务调度器】进行配置后启动服务，链接地址为【任务调度器】部署地址。', '0', 'http://localhost:8081/snail-job-admin', NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1842179763459522561, '1777536311941824513', '用户编辑', '用户编辑', 'perms', NULL, NULL, '0', '0', 'system:user:modify', NULL, 1, 1, '2024-10-04 20:27:36', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1842179829247180801, '1777536058626834433', '角色编辑', '角色编辑', 'perms', NULL, NULL, '0', '0', 'system:role:modify', NULL, 1, 1, '2024-10-04 20:27:52', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1842179926320152578, '1897209379781541890', '菜单编辑', '菜单编辑', 'perms', NULL, NULL, '0', '0', 'system:menu:modify', NULL, 1, 1, '2024-10-04 20:28:15', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1842179977343860738, '1777536895235293186', '部门编辑', '部门编辑', 'perms', NULL, NULL, '0', '0', 'system:dept:modify', NULL, 1, 1, '2024-10-04 20:28:27', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1842180022327771137, '1784084466574819330', '岗位编辑', '岗位编辑', 'perms', NULL, NULL, '0', '0', 'system:post:modify', NULL, 1, 1, '2024-10-04 20:28:38', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1842180212346519554, '1838487275271753729', '用户强退', '用户强退', 'perms', NULL, NULL, '0', '0', 'system:loginUser:clear', NULL, 1, 1, '2024-10-04 20:29:23', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1842180329170468866, '1840684744634474498', '删除缓存', '删除缓存', 'perms', NULL, NULL, '0', '0', 'system:cache:delete', NULL, 1, 1, '2024-10-04 20:29:51', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1866101773239513090, '0', '系统组件', '系统组件', 'directory', '/component', NULL, '0', '0', 'directory', 'SkinOutlined', 4, 1, '2024-12-09 20:45:08', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1866102220608172033, '1866101773239513090', '可展开卡片', '可展开卡片', 'page', '/expandable-card', '/component/expandable-card/ExpandedCardIndex.vue', '0', '0', 'page', 'BlockOutlined', 1, 1, '2024-12-09 20:46:55', '1', '2024-12-09 21:38:32', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1869722347316580354, '1866101773239513090', '字典标签', '字典标签', 'page', '/dict-tag', '/component/dict-tag/DictTagIndex.vue', '0', '0', 'page', 'TagOutlined', 3, 1, '2024-12-19 20:32:00', '1', '2024-12-22 17:46:53', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870098893441273858, '1866101773239513090', '简单树形选择', '简单树形选择', 'page', '/easy-tree-select', '/component/easy-tree-select/EasyTreeSelectIndex.vue', '0', '0', 'page', 'ApartmentOutlined', 5, 1, '2024-12-20 21:28:16', '1', '2024-12-22 17:47:19', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870290012938657794, '1866101773239513090', '颜色选择', '颜色选择', 'page', '/color-select', '/component/color-select/ColorSelectIndex.vue', '0', '0', 'page', 'BgColorsOutlined', 8, 1, '2024-12-21 10:07:42', '1', '2024-12-22 17:47:49', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870301196588900353, '1866101773239513090', '富文本编辑器', '富文本编辑器', 'page', '/editor', '/component/editor/EditorIndex.vue', '0', '0', 'page', 'EditOutlined', 7, 1, '2024-12-21 10:52:08', '1', '2024-12-22 17:47:35', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870319939100565506, '1866101773239513090', '图标选择', '图标选择', 'page', '/icon-select', '/component/icon-select/IconSelectIndex.vue', '0', '0', 'page', 'InfoOutlined', 9, 1, '2024-12-21 12:06:37', '1', '2024-12-22 17:47:59', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870342042247979009, '1866101773239513090', '图片裁剪', '图片裁剪', 'page', '/image-cropper', '/component/image-cropper/ImageCropperIndex.vue', '0', '0', 'page', 'GatewayOutlined', 10, 1, '2024-12-21 13:34:27', '1', '2024-12-22 17:48:10', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870463250452930562, '1866101773239513090', '全屏遮罩&Spin', '全屏遮罩&Spin', 'page', '/mask', '/component/mask/MaskIndex.vue', '0', '0', 'page', 'MacCommandOutlined', 4, 1, '2024-12-21 21:36:05', '1', '2024-12-22 17:50:18', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870661733822672897, '1866101773239513090', '可选择卡片', '可选择卡片', 'page', '/selectable-card', '/component/selectable-card/SelectableCardIndex.vue', '0', '0', 'page', 'CreditCardOutlined', 6, 1, '2024-12-22 10:44:47', '1', '2025-02-21 19:28:54', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870738350989291522, '1866101773239513090', '用户展示', '用户展示', 'page', '/user-show', '/component/user-show/UserShowIndex.vue', '0', '0', 'page', 'TeamOutlined', 2, 1, '2024-12-22 15:49:14', '1', '2024-12-22 17:46:44', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1870752914480996354, '1866101773239513090', '用户选择', '用户选择', 'page', '/user-select', '/component/user-select/UserSelectIndex.vue', '0', '0', 'page', 'ContactsOutlined', 11, 1, '2024-12-22 16:47:06', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1892898089370066945, '1866101773239513090', '附件上传', '附件上传', 'page', '/attachment', '/component/attachment/AttachmentIndex.vue', '0', '0', 'page', 'UploadOutlined', 12, 1, '2025-02-21 19:24:08', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1892898801227341825, '1775035631645659138', '附件管理', '附件管理', 'page', '/attachment', '/system/attachment/SystemAttachment.vue', '0', '0', 'page', 'ProfileOutlined', 7, 1, '2025-02-21 19:26:57', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1897209379781541890, '1775035631645659138', '菜单管理', '菜单管理', 'page', '/menu', '/system/menu/SystemMenu.vue', '0', '0', 'page', 'BarsOutlined', 3, NULL, NULL, '1', '2024-06-02 12:23:14', '0', NULL, '0', '111', NULL, '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1897209872981360641, '1775035631645659138', '字典管理', '字典管理', 'page', '/dict', '/system/dict/SystemDict.vue', '0', '0', 'page', 'ReadOutlined', 6, NULL, NULL, '1', '2024-10-04 16:59:15', '0', NULL, '0', '22222', '', '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (1909264238680707073, '1866101773239513090', '表格设置', '表格设置', 'page', '/table-setting', '/component/table-setting/TableSetting.vue', '0', '0', 'page', 'TableOutlined', 13, 1, '2025-04-07 23:17:22', '1', '2025-04-12 20:15:01', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES (2101309446872371201, '1866101773239513090', '密码强度', '密码强度', 'page', '/password-input', '/component/password-input/PasswordInputIndex.vue', '0', '0', 'page', 'LockOutlined', 14, 1, '2026-09-19 13:57:58', '1', '2026-09-19 22:23:53', '0', NULL, '0', NULL, NULL, '0', 'inner');
COMMIT;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` bigint NOT NULL COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `priority` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '优先级',
  `user_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户范围',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '文章内容',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `release_time` datetime DEFAULT NULL COMMENT '发布时间',
  `release_id` bigint DEFAULT NULL COMMENT '发布人id',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类型对应的图标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
BEGIN;
INSERT INTO `sys_notice` (`id`, `title`, `type`, `status`, `priority`, `user_scope`, `content`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `release_time`, `release_id`, `remark`, `icon`) VALUES (2102784850384203777, '欢迎使用狸花猫后台管理系统', '0', '0', '2', '0', '<h1 id=\"lihua-狸花猫-多端权限管理系统\" tabindex=\"-1\">Lihua（狸花猫）多端权限管理系统</h1>\n<p>Lihua（狸花猫）是一套基于 <strong>Spring Boot / Spring Cloud + Vue3 + uni-app</strong> 的多端权限管理解决方案，面向中后台业务场景设计。3.0 起项目按端拆分为 <strong>四个仓库</strong>：Boot 单体后端、Cloud 微服务后端、Web 管理端与移动端 App，四端共用统一的账号、权限与数据模型，支持独立演进与按需取用。</p>\n<h2 id=\"📌-项目背景\" tabindex=\"-1\">📌 项目背景</h2>\n<p>在实际业务开发中，权限体系、组织结构、系统配置等基础能力往往需要反复建设，并且随着业务发展，还需要同时适配 Web 与移动端。 狸花猫系统致力于提供一套 <strong>统一权限模型、统一数据来源、统一配置中心</strong> 的多端解决方案，降低重复开发成本，让开发者专注于业务逻辑本身。</p>\n<p>项目名称「狸花猫」来源于作者家中饲养的两只狸花猫，以品种命名作为系统主题 🐾</p>\n<h2 id=\"🆕-3-0-新变化\" tabindex=\"-1\">🆕 3.0 新变化</h2>\n<ul>\n<li><strong>仓库拆分</strong>：由 2.x 的全栈单仓拆分为四个独立仓库，后端提供 Boot 与 Cloud 双形态，前端与移动端独立成仓</li>\n<li><strong>依赖升级</strong>：Spring Boot 4.x + Java 25（虚拟线程默认开启）、Spring Cloud 2025.x + Nacos 3.x、Vue 3.5 + Ant Design Vue Next</li>\n<li><strong>能力增强</strong>：接口限流注解、登录失败锁定、权限变更「数据更新」红点提醒、附件下载 Range 断点协商、登录页时段氛围背景等</li>\n<li><strong>安全加固</strong>：全局异常固定文案防信息外泄、防重复提交支持参数排除、管理端公告预览与管理端预览链路分离</li>\n</ul>\n<div class=\"info custom-block\">\n<p class=\"custom-block-title\">提示</p>\n<p>Web 管理端与移动端可同时对接 Boot 单体版与 Cloud 微服务版后端，接口契约保持一致。</p>\n</div>\n<h2 id=\"🔗-相关资源\" tabindex=\"-1\">🔗 相关资源</h2>\n<ul>\n<li>💬 技术交流群：850464676</li>\n</ul>\n<h2 id=\"🧩-项目仓库-3-0-四端独立仓库\" tabindex=\"-1\">🧩 项目仓库（3.0 四端独立仓库）</h2>\n<table tabindex=\"0\">\n<thead>\n<tr>\n<th>端</th>\n<th>仓库</th>\n<th>说明</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td>🏗️ 后端 &middot; Boot</td>\n<td><a href=\"https://gitee.com/yukino_git/lihua\" target=\"_blank\" rel=\"noopener noreferrer\">lihua</a></td>\n<td>Spring Boot 单体版后端，开箱即用</td>\n</tr>\n<tr>\n<td>🕸️ 后端 &middot; Cloud</td>\n<td><a href=\"https://gitee.com/yukino_git/lihua-cloud\" target=\"_blank\" rel=\"noopener noreferrer\">lihua-cloud</a></td>\n<td>Spring Cloud 微服务版后端</td>\n</tr>\n<tr>\n<td>💻 前端 &middot; Web</td>\n<td><a href=\"https://gitee.com/yukino_git/lihua-web\" target=\"_blank\" rel=\"noopener noreferrer\">lihua-web</a></td>\n<td>Vue3 管理端，可共用双形态后端</td>\n</tr>\n<tr>\n<td>📱 移动端 &middot; App</td>\n<td><a href=\"https://gitee.com/yukino_git/lihua-app\" target=\"_blank\" rel=\"noopener noreferrer\">lihua-app</a></td>\n<td>uni-app 移动端，适配 App / 小程序</td>\n</tr>\n</tbody>\n</table>\n<blockquote>\n<p>更多仓库请访问作者主页：<a href=\"https://gitee.com/yukino_git\" target=\"_blank\" rel=\"noopener noreferrer\">https://gitee.com/yukino_git</a></p>\n</blockquote>\n<h2 id=\"🧭-多端架构概览\" tabindex=\"-1\">🧭 多端架构概览</h2>\n<ul>\n<li>🏗️ <strong>后端双形态</strong>：Spring Boot 单体快速起步，Spring Cloud 微服务弹性扩展，业务代码同源</li>\n<li>💻 <strong>Web 管理端</strong>：系统管理、权限配置、业务配置中心</li>\n<li>📱 <strong>移动端 App</strong>：适配 Android、iOS、鸿蒙与微信小程序，业务能力与 Web 端对齐</li>\n</ul>\n<p>各端共用统一的后端服务与权限模型，避免能力割裂。</p>\n<h2 id=\"🚀-核心能力-web-管理端\" tabindex=\"-1\">🚀 核心能力（Web 管理端）</h2>\n<h3 id=\"🔑-权限与组织体系\" tabindex=\"-1\">🔑 权限与组织体系</h3>\n<ul>\n<li>完整的 <strong>RBAC（基于角色的访问控制）</strong> 权限模型</li>\n<li>菜单管理、角色管理、用户管理</li>\n<li>部门管理、岗位管理</li>\n<li>用户支持 <strong>多部门归属</strong>，并可指定 <strong>默认部门</strong></li>\n<li>前后端均提供接口获取用户默认部门信息，适用于复杂组织结构</li>\n</ul>\n<h3 id=\"📚-字典管理\" tabindex=\"-1\">📚 字典管理</h3>\n<ul>\n<li>支持 <strong>普通字典</strong> 与 <strong>树形字典</strong></li>\n<li>后端提供字典获取与翻译工具类</li>\n<li>前端内置 <code>dict-tag</code> 组件，可通过字典 <code>value</code> 自动展示 <code>label</code> 并匹配样式</li>\n</ul>\n<h3 id=\"📢-通知公告\" tabindex=\"-1\">📢 通知公告</h3>\n<ul>\n<li>集成富文本编辑器（TinyMCE）</li>\n<li>基于 <strong>WebSocket</strong> 实现消息实时推送</li>\n<li>支持公告发布、实时接收与历史查看</li>\n</ul>\n<h3 id=\"👤-个人中心\" tabindex=\"-1\">👤 个人中心</h3>\n<ul>\n<li>支持系统主题、布局与导航模式配置</li>\n<li>个性化设置即时生效，并同步到服务端</li>\n<li>提升整体使用体验与可定制性</li>\n</ul>\n<h3 id=\"⚙️-系统设置\" tabindex=\"-1\">⚙️ 系统设置</h3>\n<p>管理员可通过系统设置模块统一管理安全与行为策略，包括：</p>\n<ul>\n<li>默认密码规则</li>\n<li>定期修改密码策略</li>\n<li>同账号多端登录限制</li>\n<li>自助注册开关</li>\n<li>登录验证码启停</li>\n<li>IP 黑名单</li>\n<li>灰色模式支持</li>\n</ul>\n<h3 id=\"📊-运行与监控\" tabindex=\"-1\">📊 运行与监控</h3>\n<ul>\n<li>操作日志、登录日志</li>\n<li>在线用户监控</li>\n<li>缓存监控</li>\n<li>服务运行状态监控</li>\n<li>定时任务管理</li>\n</ul>\n<h2 id=\"📱-移动端能力-lihua-app\" tabindex=\"-1\">📱 移动端能力（Lihua App）</h2>\n<p>Lihua App 是基于 <strong>uni-app</strong> 的移动端业务扩展方案，与 Web 管理端保持统一的数据模型与权限体系。</p>\n<h3 id=\"✨-功能特性\" tabindex=\"-1\">✨ 功能特性</h3>\n<ul>\n<li>🔐 <strong>注册与登录</strong>\n<ul>\n<li>App / 小程序支持密码加解密</li>\n<li>注册与登录策略由 Web 端统一控制</li>\n</ul>\n</li>\n<li>🧠 <strong>验证码能力</strong>\n<ul>\n<li>集成 <strong>tianai 验证码</strong></li>\n<li>是否启用由 Web 端统一配置</li>\n</ul>\n</li>\n<li>👤 <strong>个人中心</strong>\n<ul>\n<li>用户头像、昵称等信息与后端保持一致</li>\n<li>与 Web 端用户数据实时同步</li>\n</ul>\n</li>\n<li>🛡️ <strong>权限体系</strong>\n<ul>\n<li>支持角色、权限标识、部门标识</li>\n<li><code>user store</code> 可直接获取当前用户权限信息</li>\n</ul>\n</li>\n<li>🔔 <strong>通知公告</strong>\n<ul>\n<li>基于 WebSocket 的实时消息推送</li>\n<li>App 端支持原生通知提醒</li>\n</ul>\n</li>\n<li>🌗 <strong>暗色模式</strong>\n<ul>\n<li>App 支持手动切换</li>\n<li>微信小程序可跟随系统主题</li>\n</ul>\n</li>\n</ul>\n<h2 id=\"🛠-技术架构与环境\" tabindex=\"-1\">🛠 技术架构与环境</h2>\n<h3 id=\"🧱-技术栈\" tabindex=\"-1\">🧱 技术栈</h3>\n<ul>\n<li>后端：Spring Boot 4.x / Spring Cloud 2025.x</li>\n<li>Web 前端：Vue 3 + Ant Design Vue Next</li>\n<li>移动端：uni-app（Vue 3）</li>\n</ul>\n<h3 id=\"⚡-环境要求\" tabindex=\"-1\">⚡ 环境要求</h3>\n<ul>\n<li>\n<p>Java 25</p>\n<blockquote>\n<p>Java 21 可运行但需自行验证虚拟线程相关配置</p>\n</blockquote>\n</li>\n<li>\n<p>MySQL 8.0+</p>\n</li>\n<li>\n<p>Redis</p>\n</li>\n<li>\n<p>Node.js 22+（Web 管理端）</p>\n</li>\n</ul>\n<h2 id=\"🧩-使用与扩展\" tabindex=\"-1\">🧩 使用与扩展</h2>\n<p>狸花猫系统遵循模块化、低耦合设计，适合作为：</p>\n<ul>\n<li>🏢 企业级后台管理系统基础框架</li>\n<li>🧱 多端业务系统的权限与组织底座</li>\n<li>📖 Spring Boot / Cloud + Vue3 + uni-app 多端架构参考项目</li>\n</ul>\n<p>支持在此基础上进行功能裁剪与深度定制。</p>', '0', 1, '2026-09-23 23:39:11', NULL, NULL, NULL, NULL, NULL, 'MessageOutlined');
COMMIT;

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
  `id` bigint NOT NULL COMMENT '主键',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务描述',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务类型编码',
  `type_msg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务类型描述',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类名',
  `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '方法名',
  `ip_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'ip地址',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id（操作人）',
  `create_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人昵称',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间（操作时间）',
  `execute_time` int DEFAULT NULL COMMENT '执行时长ms',
  `params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '参数',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '返回值',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '异常信息',
  `error_stack` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '堆栈信息',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求url',
  `trace_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '链路追踪id（入口生成，日志文件按此串联）',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户代理字符串，包含客户端操作系统、浏览器内核等信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除',
  `execute_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '日志执行状态',
  `cache_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户缓存key',
  `client_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '客户端类型',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'ip归属地',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统登录日志';

-- ----------------------------
-- Records of sys_operate_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `id` bigint NOT NULL COMMENT ' 主键',
  `dept_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '部门主键',
  `dept_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '部门编码',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '编码',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `manager` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '负责人',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `fax` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '传真',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统单位/岗位表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` (`id`, `dept_id`, `dept_code`, `name`, `code`, `sort`, `status`, `manager`, `phone_number`, `email`, `fax`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES (1842129333329264642, '1810226204790657025', 'lihua', '研发岗', 'lihua_dev', 1, '0', '', '', '', NULL, '0', 1, '2024-10-04 17:07:13', 1, '2026-09-22 08:44:38', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色编码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `name`, `code`, `status`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES (1, '超级管理员', 'ROLE_admin', '0', '0', NULL, '2024-05-16 21:32:55', 1, '2024-06-19 22:49:48', NULL);
INSERT INTO `sys_role` (`id`, `name`, `code`, `status`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES (1842149851067514881, '访客用户', 'ROLE_visitor', '0', '0', 1, '2024-10-04 18:28:45', 1, '2026-09-22 14:19:29', '');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1775035631645659138);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1776948212783730690);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1777536058626834433);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1777536311941824513);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1777536895235293186);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1777538040162844673);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1777538768721838082);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1777539832263114753);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1784084466574819330);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1897209379781541890);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 1897209872981360641);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1775035631645659138);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1776948212783730690);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1777536058626834433);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1777536311941824513);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1777536895235293186);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1777538040162844673);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1777538768721838082);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1777539832263114753);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1784084466574819330);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1823518320191033346);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1838036266821033985);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1838487275271753729);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1838581685913554946);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1840684744634474498);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1841350424845307905);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1842179763459522561);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1842179829247180801);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1842179926320152578);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1842179977343860738);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1842180022327771137);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1842180212346519554);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1842180329170468866);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1866101773239513090);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1866102220608172033);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1869722347316580354);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870098893441273858);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870290012938657794);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870301196588900353);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870319939100565506);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870342042247979009);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870463250452930562);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870661733822672897);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870738350989291522);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1870752914480996354);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1892898089370066945);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1892898801227341825);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1897209379781541890);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1897209872981360641);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1842149851067514881, 1909264238680707073);
COMMIT;

-- ----------------------------
-- Table structure for sys_setting
-- ----------------------------
DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting` (
  `setting_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设置key（主键）',
  `json` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设置参数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint DEFAULT NULL COMMENT '更新id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`setting_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户和登录后设置关联表';

-- ----------------------------
-- Records of sys_setting
-- ----------------------------
BEGIN;
INSERT INTO `sys_setting` (`setting_key`, `json`, `create_time`, `create_id`, `update_time`, `update_id`, `del_flag`) VALUES ('CaptchaSetting', '{\"enable\":false}', '2026-08-27 20:37:38', 1, '2026-09-23 19:16:37', 1, '0');
INSERT INTO `sys_setting` (`setting_key`, `json`, `create_time`, `create_id`, `update_time`, `update_id`, `del_flag`) VALUES ('DefaultPasswordSetting', '{\"defaultPassword\":\"123456\"}', '2026-04-21 10:35:36', 1, '2026-09-24 00:17:49', 1, '0');
INSERT INTO `sys_setting` (`setting_key`, `json`, `create_time`, `create_id`, `update_time`, `update_id`, `del_flag`) VALUES ('GrayModelSetting', '{\"enable\":false}', '2026-04-23 13:26:15', 1, '2026-09-23 19:22:59', 1, '0');
INSERT INTO `sys_setting` (`setting_key`, `json`, `create_time`, `create_id`, `update_time`, `update_id`, `del_flag`) VALUES ('IntervalUpdatePasswordSetting', '{\"enable\":false,\"unit\":\"month\"}', '2026-08-27 20:37:25', 1, '2026-09-23 19:15:37', 1, '0');
INSERT INTO `sys_setting` (`setting_key`, `json`, `create_time`, `create_id`, `update_time`, `update_id`, `del_flag`) VALUES ('RestrictAccessIpSetting', '{\"enable\":false,\"ipList\":[\"\"]}', '2026-04-02 17:02:48', 1, '2026-09-23 03:10:56', 1, '0');
INSERT INTO `sys_setting` (`setting_key`, `json`, `create_time`, `create_id`, `update_time`, `update_id`, `del_flag`) VALUES ('SameAccountLoginSetting', '{\"enable\":false,\"maximum\":1}', '2026-08-27 20:37:33', 1, '2026-09-23 19:25:34', 1, '0');
INSERT INTO `sys_setting` (`setting_key`, `json`, `create_time`, `create_id`, `update_time`, `update_id`, `del_flag`) VALUES ('SignUpSetting', '{\"enable\":false,\"deptIds\":[],\"defaultDeptId\":\"\",\"postIds\":[],\"roleIds\":[]}', '2026-04-21 16:27:38', 1, '2026-09-23 22:50:51', 1, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '性别',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户状态',
  `theme` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户系统主题json',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `password_update_time` datetime DEFAULT NULL COMMENT '密码更新时间',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号码',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `register_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '注册类型 0 管理员新增，1 用户自助注册',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_sys_user_username` (`username`) USING BTREE,
  KEY `idx_sys_user_email` (`email`) USING BTREE,
  KEY `idx_sys_user_phone_number` (`phone_number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `avatar`, `gender`, `status`, `theme`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `password_update_time`, `email`, `phone_number`, `remark`, `register_type`) VALUES (1, 'admin', '$2a$10$pLNwhfNEckhXuwd3nTkPKec3J9JrYgz560F.ixtdkBNX9Y0gEbJwi', 'admin', '{\"value\":\"狸花猫\",\"type\":\"text\",\"backgroundColor\":\"rgb(47, 84, 235)\"}', '1', '0', '{\"layoutType\":\"side-navigation\",\"componentSize\":\"default\",\"showViewTabs\":true,\"showFooter\":true,\"colorPrimary\":\"rgb(22, 119, 255)\",\"antColorPrimary\":\"rgb(22, 119, 255)\",\"borderRadius\":6,\"siderTheme\":\"light\",\"groundGlass\":true,\"affixHead\":true,\"siderGroup\":false,\"siderWith\":240,\"originSiderWith\":240,\"routeTransition\":\"zoom\",\"clickEffect\":\"wave\",\"grayModel\":false,\"themeConfig\":{\"token\":{\"colorPrimary\":\"rgb(22, 119, 255)\",\"borderRadius\":6}}}', '0', NULL, '2024-06-02 16:57:25', 1, '2026-09-24 00:17:34', '2026-09-24 00:17:34', '2651518588@qq.com', '15510916240', NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `create_time` datetime DEFAULT NULL COMMENT '绑定时间',
  `create_id` bigint DEFAULT NULL COMMENT '绑定人id',
  `default_dept` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '默认单位',
  PRIMARY KEY (`user_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户部门关联表';

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`, `create_time`, `create_id`, `default_dept`) VALUES (1, 1810226204790657025, '2026-09-23 21:48:05', 1, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_notice`;
CREATE TABLE `sys_user_notice` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `notice_id` bigint NOT NULL COMMENT '公告id',
  `star_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'star标记',
  `read_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '已读标记',
  `read_time` datetime DEFAULT NULL COMMENT '已读时间',
  PRIMARY KEY (`user_id`,`notice_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户通知关联表';

-- ----------------------------
-- Records of sys_user_notice
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `post_id` bigint NOT NULL COMMENT '岗位id',
  `create_time` datetime DEFAULT NULL COMMENT '绑定时间',
  `create_id` bigint DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '绑定时间',
  `create_id` bigint DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_time`, `create_id`) VALUES (1, 1, '2024-06-02 16:57:25', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_view_tab
-- ----------------------------
DROP TABLE IF EXISTS `sys_view_tab`;
CREATE TABLE `sys_view_tab` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `affix` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否固定（1固定，0不固定）',
  `star` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否收藏（1收藏，0不收藏）',
  PRIMARY KEY (`user_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户菜单收藏管理表';

-- ----------------------------
-- Records of sys_view_tab
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
