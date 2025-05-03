/*
 Navicat Premium Dump SQL

 Source Server         : MySql
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3306
 Source Schema         : lihua

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 05/03/2025 17:06:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment`  (
  `id` bigint NOT NULL COMMENT '主键',
  `storage_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件数据库名',
  `original_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件原名称',
  `extension_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件保存路径',
  `upload_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分片上传id',
  `business_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务编码',
  `business_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务名称',
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件大小',
  `type` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
  `upload_mode` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上传方式（一般上传、分片上传、文件秒传）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上传状态（成功、失败、分片上传中、业务删除）',
  `storage_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件存储位置',
  `md5` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件md5值',
  `create_id` bigint NULL DEFAULT NULL COMMENT '上传人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '删除标识',
  `error_msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上传失败原因',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原url（通过url上传有该字段）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统附件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL COMMENT ' 主键',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级id',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编码',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `manager` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `fax` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '传真',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统单位/岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1810226204790657025, 0, '狸花猫科技', 'lihua', 1, '0', '', NULL, NULL, NULL, '0', 1, '2024-07-08 16:15:34', 1, '2024-10-04 17:02:16', NULL);
INSERT INTO `sys_dept` VALUES (1842128409600917505, 1810226204790657025, '猫粮生产中心', 'lihua_food', 1, '0', '小黑子', NULL, NULL, NULL, '0', 1, '2024-10-04 17:03:33', 1, '2024-10-04 17:05:47', NULL);
INSERT INTO `sys_dept` VALUES (1842128590274756609, 1810226204790657025, '冻干生产中心', 'lihua_freeze_dried', 2, '0', '小黑子', NULL, NULL, NULL, '0', 1, '2024-10-04 17:04:16', 1, '2024-10-04 17:05:55', NULL);
INSERT INTO `sys_dept` VALUES (1842128734768529409, 1810226204790657025, '逗猫棒生产中心', 'lihua_teaser', 3, '0', '小喵子', NULL, NULL, NULL, '0', 1, '2024-10-04 17:04:50', 1, '2024-10-04 17:06:01', NULL);
INSERT INTO `sys_dept` VALUES (1842128938393600002, 1810226204790657025, '猫窝生产中心', 'lihua_cattery', 4, '0', '小喵子', NULL, NULL, NULL, '0', 1, '2024-10-04 17:05:39', 1, '2024-10-04 17:06:10', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级字典id',
  `dict_type_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型编码',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典标签',
  `value` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后一次更新时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `tag_style` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签的样式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1771163317281083393, 0, 'sys_status', '正常', '0', 1, NULL, '0', 1, '2024-03-22 21:13:36', 1, '2024-03-24 13:42:09', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1771163394133315585, 0, 'sys_status', '停用', '1', 2, NULL, '0', 1, '2024-03-22 21:13:54', 1, '2024-03-22 21:18:01', '0', 'error');
INSERT INTO `sys_dict_data` VALUES (1771165766595235841, 0, 'sys_dict_type', '一般字典', '0', 1, NULL, '0', 1, '2024-03-22 21:23:20', 1, '2024-03-24 20:46:11', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1771165768948240385, 0, 'sys_dict_type', '树型字典', '1', 2, NULL, '0', 1, '2024-03-22 21:23:21', 1, '2024-03-24 20:46:12', '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1771166154488664065, 0, 'sys_dict_tag_style', '默认', 'default', 1, NULL, '0', 1, '2024-03-22 21:24:53', 1, '2024-07-05 15:25:08', '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1771166156803919874, 0, 'sys_dict_tag_style', '主要', 'processing', 2, NULL, '0', 1, '2024-03-22 21:24:53', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1771166159454720001, 0, 'sys_dict_tag_style', '成功', 'success', 3, NULL, '0', 1, '2024-03-22 21:24:54', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1771166162894049282, 0, 'sys_dict_tag_style', '警告', 'warning', 4, NULL, '0', 1, '2024-03-22 21:24:55', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` VALUES (1771166168740909057, 0, 'sys_dict_tag_style', '错误', 'error', 5, NULL, '0', 1, '2024-03-22 21:24:56', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` VALUES (1772463094826942465, 0, 'sys_dict_tag_style', '红色', '#ff4d4f', 7, NULL, '0', 1, '2024-03-26 11:18:27', 1, '2024-07-21 20:57:03', '0', '#ff4d4f');
INSERT INTO `sys_dict_data` VALUES (1773701158649315330, 0, 'sys_menu_type', '目录', 'directory', 1, NULL, '0', 1, '2024-03-29 21:18:05', 1, '2024-03-30 22:40:29', '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1773701160050212865, 0, 'sys_menu_type', '页面', 'page', 2, NULL, '0', 1, '2024-03-29 21:18:05', 1, '2024-04-05 11:07:54', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1773701161455304706, 0, 'sys_menu_type', '权限', 'perms', 4, NULL, '0', 1, '2024-03-29 21:18:05', 1, '2024-04-07 20:17:27', '0', 'warning');
INSERT INTO `sys_dict_data` VALUES (1774046926568538113, 0, 'sys_menu_type', '链接', 'link', 3, NULL, '0', 1, '2024-03-30 20:12:02', 1, '2024-04-05 11:08:03', '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1774250426447544321, 0, 'sys_link_menu_open_type', '系统内', 'inner', 1, NULL, '0', 1, '2024-03-31 09:40:40', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1774250428016214017, 0, 'sys_link_menu_open_type', '新页面', 'new-page', 2, NULL, '0', 1, '2024-03-31 09:40:41', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1774252801971306497, 0, 'sys_whether', '是', '0', 1, NULL, '0', 1, '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1774252803737108481, 0, 'sys_whether', '否', '1', 2, NULL, '0', 1, '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1780864938674667522, 0, 'sys_dept_type', '部门', 'dept', 1, NULL, '0', 1, '2024-04-18 15:44:23', 1, '2024-04-20 21:35:37', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1780864940843122690, 0, 'sys_dept_type', '岗位', 'post', 2, NULL, '0', 1, '2024-04-18 15:44:23', 1, '2024-04-20 21:35:37', '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1794263233540739074, 0, 'user_gender', '女', '0', 2, NULL, '0', 1, '2024-05-25 15:04:25', 1, '2024-05-25 15:08:41', '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1794263235541422081, 0, 'user_gender', '男', '1', 1, NULL, '0', 1, '2024-05-25 15:04:26', 1, '2024-05-25 15:08:41', '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1794263238364188674, 0, 'user_gender', '不愿透露', '2', 3, NULL, '0', 1, '2024-05-25 15:04:26', 1, '2024-09-25 19:22:13', '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1814191392971739137, 0, 'sys_notice_status', '未发布', '0', 1, NULL, '0', 1, '2024-07-19 14:51:49', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1814191395400241153, 0, 'sys_notice_status', '已发布', '1', 2, NULL, '0', 1, '2024-07-19 14:51:49', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1814191397560307713, 0, 'sys_notice_status', '已撤销', '2', 3, NULL, '0', 1, '2024-07-19 14:51:50', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` VALUES (1814191691274833922, 0, 'sys_notice_type', '通知', '0', 1, NULL, '0', 1, '2024-07-19 14:53:00', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1814191692868669441, 0, 'sys_notice_type', '公告', '1', 2, NULL, '0', 1, '2024-07-19 14:53:00', 1, '2024-07-20 17:53:15', '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1814602689115856897, 0, 'sys_notice_user_scope', '全部用户', '0', 1, NULL, '0', 1, '2024-07-20 18:06:09', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1814602691913457666, 0, 'sys_notice_user_scope', '指定用户', '1', 2, NULL, '0', 1, '2024-07-20 18:06:10', 1, '2024-07-21 22:04:21', '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1814603526915497986, 0, 'sys_notice_priority', '紧急', '0', 1, NULL, '0', 1, '2024-07-20 18:09:29', 1, '2024-07-21 20:59:43', '0', '#ff4d4f');
INSERT INTO `sys_dict_data` VALUES (1814603528811323393, 0, 'sys_notice_priority', '高', '1', 2, NULL, '0', 1, '2024-07-20 18:09:30', 1, '2024-07-21 20:59:44', '0', '#faad14');
INSERT INTO `sys_dict_data` VALUES (1814603531445346306, 0, 'sys_notice_priority', '中', '2', 3, NULL, '0', 1, '2024-07-20 18:09:30', 1, '2024-07-21 20:59:44', '0', '#1677ff');
INSERT INTO `sys_dict_data` VALUES (1814603537514504193, 0, 'sys_notice_priority', '低', '3', 4, NULL, '0', 1, '2024-07-20 18:09:32', 1, '2024-07-21 20:59:45', '0', '#52c41a');
INSERT INTO `sys_dict_data` VALUES (1815008261946531841, 0, 'sys_dict_tag_style', '橙色', '#faad14', 8, NULL, '0', 1, '2024-07-21 20:57:46', NULL, NULL, '0', '#faad14');
INSERT INTO `sys_dict_data` VALUES (1815008480134225921, 0, 'sys_dict_tag_style', '蓝色', '#1677ff', 9, NULL, '0', 1, '2024-07-21 20:58:38', NULL, NULL, '0', '#1677ff');
INSERT INTO `sys_dict_data` VALUES (1815008645746319361, 0, 'sys_dict_tag_style', '绿色', '#52c41a', 10, NULL, '0', 1, '2024-07-21 20:59:17', NULL, NULL, '0', '#52c41a');
INSERT INTO `sys_dict_data` VALUES (1823522986106695682, 0, 'sys_log_status', '成功', '0', 1, NULL, '0', 1, '2024-08-14 08:52:14', 1, '2024-08-14 08:52:43', '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1823522988254179330, 0, 'sys_log_status', '失败', '1', 2, NULL, '0', 1, '2024-08-14 08:52:14', 1, '2024-08-14 08:52:43', '0', 'error');
INSERT INTO `sys_dict_data` VALUES (1840380919319064578, 0, 'sys_user_register_type', '管理员注册', '0', 1, NULL, '0', 1, '2024-09-29 21:19:38', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1840380921978253313, 0, 'sys_user_register_type', '用户自助注册', '1', 2, NULL, '0', 1, '2024-09-29 21:19:39', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1869716623098253313, 0, 'test_tree', '节点1', '1', 1, NULL, '0', 1, '2024-12-19 20:09:15', 1, '2025-01-17 16:14:47', '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1869716680958676994, 1869716623098253313, 'test_tree', '节点1-1', '1-1', 1, NULL, '0', 1, '2024-12-19 20:09:29', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1869716725632208898, 1869716623098253313, 'test_tree', '节点1-2', '1-2', 2, NULL, '0', 1, '2024-12-19 20:09:40', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1869716755554373633, 0, 'test_tree', '节点2', '2', 2, NULL, '0', 1, '2024-12-19 20:09:47', 1, '2025-01-17 16:14:48', '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1869716801964347394, 1869716755554373633, 'test_tree', '节点2-1', '2-1', 1, NULL, '0', 1, '2024-12-19 20:09:58', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1869716862974693377, 1869716755554373633, 'test_tree', '节点2-2', '2-2', 2, NULL, '0', 1, '2024-12-19 20:10:12', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1869719177509081090, 1869716862974693377, 'test_tree', '节点2-2-1', '2-2-1', 1, NULL, '0', 1, '2024-12-19 20:19:24', 1, '2024-12-19 20:52:53', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1880166202906210305, 0, 'sys_user_register_type', '批量导入注册', '2', 3, NULL, '0', 1, '2025-01-17 16:12:09', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1892896495786205185, 0, 'sys_attachment_upload_mode', '一般上传', '0', 1, NULL, '0', 1, '2025-02-21 19:17:48', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1892896497644281857, 0, 'sys_attachment_upload_mode', '分片上传', '1', 2, NULL, '0', 1, '2025-02-21 19:17:48', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` VALUES (1892896500349607937, 0, 'sys_attachment_upload_mode', '文件秒传', '2', 3, NULL, '0', 1, '2025-02-21 19:17:49', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1892896502400622593, 0, 'sys_attachment_upload_mode', 'URL上传', '3', 4, NULL, '0', 1, '2025-02-21 19:17:49', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES (1892896965527281665, 0, 'sys_attachment_status', '上传成功', '0', 1, NULL, '0', 1, '2025-02-21 19:19:40', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES (1892896966877847554, 0, 'sys_attachment_status', '上传失败', '1', 2, NULL, '0', 1, '2025-02-21 19:19:40', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` VALUES (1892896969004359681, 0, 'sys_attachment_status', '分片上传中', '2', 3, NULL, '0', 1, '2025-02-21 19:19:40', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES (1892896971588050945, 0, 'sys_attachment_status', '业务删除', '3', 4, NULL, '0', 1, '2025-02-21 19:19:41', NULL, NULL, '0', 'warning');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型名称',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型编码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典备注',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后一次更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1771163166122561537, '系统状态', 'sys_status', '0', '系统通用状态标识', 1, '2024-03-22 21:13:00', 1, '2024-07-19 15:27:12', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1771164641267666946, '字典标签样式', 'sys_dict_tag_style', '0', '字典配置配置中，样式列的字典', 1, '2024-03-22 21:18:52', 1, '2024-03-22 21:20:00', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1771165529122131969, '字典类型', 'sys_dict_type', '0', '区分字典为一般字典还是树型字典', 1, '2024-03-22 21:22:23', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES (1773700957867982850, '菜单类型', 'sys_menu_type', '0', '系统菜单配置类型，分为 目录、页面、链接、权限', 1, '2024-03-29 21:17:17', 1, '2024-04-01 09:59:35', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1774249964684034050, '链接菜单打开方式', 'sys_link_menu_open_type', '0', '链接菜单打开方式，分为系统内嵌套和浏览器新页面', 1, '2024-03-31 09:38:50', 1, '2024-03-31 09:41:45', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1774252683993923586, '系统是否', 'sys_whether', '0', '系统是否选项字典', 1, '2024-03-31 09:49:39', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES (1780864852875984898, '部门类型', 'sys_dept_type', '0', '保存部门操作时的类型选项，分为部门和岗位', 1, '2024-04-18 15:44:02', 1, '2024-04-20 21:35:36', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1794262937292853250, '用户性别', 'user_gender', '0', '系统用户性别字典', 1, '2024-05-25 15:03:15', 1, '2024-05-25 15:03:26', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1814191109734584322, '公告状态', 'sys_notice_status', '0', '系统公告状态字典', 1, '2024-07-19 14:50:41', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES (1814191516905033729, '公告类型', 'sys_notice_type', '0', '系统公告类型字典', 1, '2024-07-19 14:52:18', 1, '2024-07-19 14:52:32', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1814602561218945026, '用户范围', 'sys_notice_user_scope', '0', '通知公告接收消息的用户范围', 1, '2024-07-20 18:05:39', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES (1814603011422953473, '优先级别', 'sys_notice_priority', '0', '通知公告优先程度', 1, '2024-07-20 18:07:26', 1, '2024-07-29 22:13:21', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1823522921661214721, '日志执行结果', 'sys_log_status', '0', '日志记录程序执行是否成功', 1, '2024-08-14 08:51:59', 1, '2024-09-23 09:05:30', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1840380676900876290, '用户注册类型', 'sys_user_register_type', '0', '用户注册类型：管理员注册、用户自助注册、批量导入注册', 1, '2024-09-29 21:18:41', 1, '2025-01-17 16:12:24', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1869716543582638081, '树形字典测试', 'test_tree', '1', '测试用，可删除', 1, '2024-12-19 20:08:56', 1, '2025-02-21 19:19:57', '0', '0');
INSERT INTO `sys_dict_type` VALUES (1892895886177673218, '附件状态', 'sys_attachment_status', '0', '系统附件上传状态', 1, '2025-02-21 19:15:22', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES (1892896049893941249, '上传方式', 'sys_attachment_upload_mode', '0', '系统附件上传方式', 1, '2025-02-21 19:16:01', NULL, NULL, '0', '0');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务描述',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型编码',
  `type_msg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型描述',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名',
  `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名',
  `ip_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `create_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人昵称',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id（操作人）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间（操作时间）',
  `execute_time` int NULL DEFAULT NULL COMMENT '执行时长ms',
  `params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回值',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异常信息',
  `error_stack` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '堆栈信息',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求url',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理字符串，包含客户端操作系统、浏览器内核等信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除',
  `execute_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志执行状态',
  `cache_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户缓存key',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `cache_key`(`cache_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父级菜单id',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '鼠标悬浮展示内容',
  `menu_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单/页面/按钮/外链',
  `router_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组建路径',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否显示（0显示、1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单状态(0正常、1停用)',
  `perms` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识符',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后一次更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否缓存页面（0 缓存、1不缓存）',
  `link_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外链类型页面地址',
  `query` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由携带的参数',
  `view_tab` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '允许view-tab显示标签',
  `link_open_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接打开方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1775035631645659138, '0', '系统管理', '系统管理', 'directory', '/system', NULL, '0', '0', 'directory', 'SettingOutlined', 2, 1, '2024-04-02 13:40:48', '1', '2024-06-19 22:49:12', '0', NULL, '1', NULL, NULL, '0', 'new-page');
INSERT INTO `sys_menu` VALUES (1776948212783730690, '1897209872981360641', '字典编辑', '字典编辑', 'perms', '', NULL, '0', '0', 'system:dict:modify', NULL, 1, 1, '2024-04-07 20:20:43', '1', '2024-10-04 20:45:15', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1777536058626834433, '1775035631645659138', '角色管理', '角色管理', 'page', '/role', '/system/role/SystemRole.vue', '0', '0', 'page', 'TeamOutlined', 2, 1, '2024-04-09 11:16:36', '1', '2024-06-02 12:23:03', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1777536311941824513, '1775035631645659138', '用户管理', '用户管理', 'page', '/user', '/system/user/SystemUser.vue', '0', '0', 'page', 'UserSwitchOutlined', 1, 1, '2024-04-09 11:17:36', '1', '2024-06-02 12:22:57', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1777536895235293186, '1775035631645659138', '部门管理', '部门管理', 'page', '/dept', '/system/dept/SystemDept.vue', '0', '0', 'page', 'ApartmentOutlined', 4, 1, '2024-04-09 11:19:56', '1', '2024-06-02 12:23:50', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1777538040162844673, '0', '日志管理', '日志管理', 'directory', '/log', NULL, '0', '0', 'directory', 'FileSearchOutlined', 3, 1, '2024-04-09 11:24:28', '1', '2024-11-13 21:04:51', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1777538768721838082, '1777538040162844673', '登录日志', '登录日志', 'page', '/login', '/system/log/SystemLoginLog.vue', '0', '0', 'page', 'FileProtectOutlined', 1, 1, '2024-04-09 11:27:22', '1', '2024-11-13 21:04:51', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1777539832263114753, '1775035631645659138', '通知公告', '通知公告', 'page', '/system/notice', '/system/notice/SystemNotice.vue', '0', '0', 'page', 'MessageOutlined', 8, 1, '2024-04-09 11:31:36', '1', '2025-02-21 19:27:12', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1784084466574819330, '1775035631645659138', '岗位管理', '岗位管理', 'page', '/post', '/system/post/SystemPost.vue', '0', '0', 'page', 'ScheduleOutlined', 5, 1, '2024-04-27 12:57:38', '1', '2024-06-16 18:46:09', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1823518320191033346, '1777538040162844673', '操作日志', '操作日志', 'page', '/operate', '/system/log/SystemOperateLog.vue', '0', '0', 'page', 'FileSyncOutlined', 2, 1, '2024-08-14 08:33:42', '1', '2024-11-13 21:04:52', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1838036266821033985, '0', '系统监控', '系统监控', 'directory', '/monitor', NULL, '0', '0', 'directory', 'FundViewOutlined', 4, 1, '2024-09-23 10:02:50', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1838036487672111105, '1838036266821033985', '数据监控', '数据监控', 'page', '/druid', '/monitor/druid/MonitorDruid.vue', '0', '1', 'link', 'DatabaseOutlined', 4, 1, '2024-09-23 10:03:42', '1', '2024-10-02 13:33:54', '0', '由druid数据库连接池提供，如需开启监控，请在后台引入druid后进行相关配置。', '0', '', NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1838487275271753729, '1838036266821033985', '在线用户', '在线用户', 'page', '/loggedUser', '/monitor/logged-user/MonitorLoggedUser.vue', '0', '0', 'page', 'UserOutlined', 0, 1, '2024-09-24 15:54:59', '1', '2024-09-24 18:23:11', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1838581685913554946, '1838036266821033985', '服务监控', '服务监控', 'page', '/server', '/monitor/server/MonitorServer.vue', '0', '0', 'page', 'CloudServerOutlined', 3, 1, '2024-09-24 22:10:08', '1', '2024-10-02 13:34:22', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1840684744634474498, '1838036266821033985', '缓存监控', '缓存监控', 'page', '/cache', '/monitor/cache/MonitorCache.vue', '0', '0', 'page', 'CodepenOutlined', 2, 1, '2024-09-30 17:26:56', '1', '2024-10-02 13:34:18', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1841350424845307905, '1838036266821033985', '定时任务', '定时任务', 'link', '/job', NULL, '0', '0', 'link', 'FieldTimeOutlined', 1, 1, '2024-10-02 13:32:07', '1', '2024-10-02 13:39:28', '0', '定时任务基于 xxl-job。 使用时请启动【任务调度器】进行配置后启动服务，链接地址为【任务调度器】部署地址。', '0', 'http://localhost:8081/xxl-job-admin', NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842060821709651969, '0', '开发文档', '开发文档', 'directory', '/document', NULL, '0', '1', 'directory', 'ProfileOutlined', 5, 1, '2024-10-04 12:34:58', '1', '2024-10-22 09:15:37', '1', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842061073367891970, '1842060821709651969', '后端文档', '后端文档', 'page', '/service', '/document/service/DocumentService.vue', '0', '1', 'page', 'CloudServerOutlined', 1, 1, '2024-10-04 12:35:58', '1', '2024-10-22 09:15:37', '1', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842061397587591169, '1842060821709651969', '前端文档', '前端文档', 'page', '/web', '/document/web/DocumentWeb.vue', '0', '1', 'page', 'GlobalOutlined', 2, 1, '2024-10-04 12:37:16', '1', '2024-10-22 09:15:37', '1', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842179763459522561, '1777536311941824513', '用户编辑', '用户编辑', 'perms', NULL, NULL, '0', '0', 'system:user:modify', NULL, 1, 1, '2024-10-04 20:27:36', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842179829247180801, '1777536058626834433', '角色编辑', '角色编辑', 'perms', NULL, NULL, '0', '0', 'system:role:modify', NULL, 1, 1, '2024-10-04 20:27:52', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842179926320152578, '1897209379781541890', '菜单编辑', '菜单编辑', 'perms', NULL, NULL, '0', '0', 'system:menu:modify', NULL, 1, 1, '2024-10-04 20:28:15', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842179977343860738, '1777536895235293186', '部门编辑', '部门编辑', 'perms', NULL, NULL, '0', '0', 'system:dept:modify', NULL, 1, 1, '2024-10-04 20:28:27', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842180022327771137, '1784084466574819330', '岗位编辑', '岗位编辑', 'perms', NULL, NULL, '0', '0', 'system:post:modify', NULL, 1, 1, '2024-10-04 20:28:38', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842180212346519554, '1838487275271753729', '用户强退', '用户强退', 'perms', NULL, NULL, '0', '0', 'system:loginUser:clear', NULL, 1, 1, '2024-10-04 20:29:23', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1842180329170468866, '1840684744634474498', '删除缓存', '删除缓存', 'perms', NULL, NULL, '0', '0', 'system:cache:delete', NULL, 1, 1, '2024-10-04 20:29:51', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1866101773239513090, '0', '系统组件', '系统组件', 'directory', '/component', NULL, '0', '0', 'directory', 'SkinOutlined', 5, 1, '2024-12-09 20:45:08', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1866102220608172033, '1866101773239513090', '可展开卡片', '可展开卡片', 'page', '/expandable-card', '/component/expandable-card/ExpandedCardIndex.vue', '0', '0', 'page', 'BlockOutlined', 1, 1, '2024-12-09 20:46:55', '1', '2024-12-09 21:38:32', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1869722347316580354, '1866101773239513090', '字典标签', '字典标签', 'page', '/dict-tag', '/component/dict-tag/DictTagIndex.vue', '0', '0', 'page', 'TagOutlined', 3, 1, '2024-12-19 20:32:00', '1', '2024-12-22 17:46:53', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870098893441273858, '1866101773239513090', '简单树形选择', '简单树形选择', 'page', '/easy-tree-select', '/component/easy-tree-select/EasyTreeSelectIndex.vue', '0', '0', 'page', 'ApartmentOutlined', 5, 1, '2024-12-20 21:28:16', '1', '2024-12-22 17:47:19', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870290012938657794, '1866101773239513090', '颜色选择', '颜色选择', 'page', '/color-select', '/component/color-select/ColorSelectIndex.vue', '0', '0', 'page', 'BgColorsOutlined', 8, 1, '2024-12-21 10:07:42', '1', '2024-12-22 17:47:49', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870301196588900353, '1866101773239513090', '富文本编辑器', '富文本编辑器', 'page', '/editor', '/component/editor/EditorIndex.vue', '0', '0', 'page', 'EditOutlined', 7, 1, '2024-12-21 10:52:08', '1', '2024-12-22 17:47:35', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870319939100565506, '1866101773239513090', '图标选择', '图标选择', 'page', '/icon-select', '/component/icon-select/IconSelectIndex.vue', '0', '0', 'page', 'InfoOutlined', 9, 1, '2024-12-21 12:06:37', '1', '2024-12-22 17:47:59', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870342042247979009, '1866101773239513090', '图片裁剪', '图片裁剪', 'page', '/image-cropper', '/component/image-cropper/ImageCropperIndex.vue', '0', '0', 'page', 'GatewayOutlined', 10, 1, '2024-12-21 13:34:27', '1', '2024-12-22 17:48:10', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870463250452930562, '1866101773239513090', '全屏遮罩&Spin', '全屏遮罩&Spin', 'page', '/mask', '/component/mask/MaskIndex.vue', '0', '0', 'page', 'MacCommandOutlined', 4, 1, '2024-12-21 21:36:05', '1', '2024-12-22 17:50:18', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870661733822672897, '1866101773239513090', '可选择卡片', '可选择卡片', 'page', '/selectable-card', '/component/selectable-card/SelectableCardIndex.vue', '0', '0', 'page', 'CreditCardOutlined', 6, 1, '2024-12-22 10:44:47', '1', '2025-02-21 19:28:54', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870738350989291522, '1866101773239513090', '用户展示', '用户展示', 'page', '/user-show', '/component/user-show/UserShowIndex.vue', '0', '0', 'page', 'TeamOutlined', 2, 1, '2024-12-22 15:49:14', '1', '2024-12-22 17:46:44', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1870752914480996354, '1866101773239513090', '用户选择', '用户选择', 'page', '/user-select', '/component/user-select/UserSelectIndex.vue', '0', '0', 'page', 'ContactsOutlined', 11, 1, '2024-12-22 16:47:06', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1892898089370066945, '1866101773239513090', '附件上传', '附件上传', 'page', '/attachment', '/component/attachment/AttachmentIndex.vue', '0', '0', 'page', 'UploadOutlined', 12, 1, '2025-02-21 19:24:08', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1892898801227341825, '1775035631645659138', '附件管理', '附件管理', 'page', '/attachment', '/system/attachment/SystemAttachment.vue', '0', '0', 'page', 'ProfileOutlined', 7, 1, '2025-02-21 19:26:57', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES (1897209379781541890, '1775035631645659138', '菜单管理', '菜单管理', 'page', '/menu', '/system/menu/SystemMenu.vue', '0', '0', 'page', 'BarsOutlined', 3, NULL, NULL, '1', '2024-06-02 12:23:14', '0', NULL, '0', '111', NULL, '0', 'new-page');
INSERT INTO `sys_menu` VALUES (1897209872981360641, '1775035631645659138', '字典管理', '字典管理', 'page', '/dict', '/system/dict/SystemDict.vue', '0', '0', 'page', 'ReadOutlined', 6, NULL, NULL, '1', '2024-10-04 16:59:15', '0', NULL, '0', '22222', '', '0', 'new-page');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint NOT NULL COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `priority` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优先级',
  `user_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户范围',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文章内容',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `release_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `release_id` bigint NULL DEFAULT NULL COMMENT '发布人id',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '如何使用Vditor', '1', '1', '3', '1', 'Vditor 是一款**所见即所得**编辑器，支持 *Markdown*。\n\n* 不熟悉 Markdown 可使用工具栏或快捷键进行排版\n* 熟悉 Markdown 可直接排版，也可切换为分屏预览\n\n更多细节和用法请参考 [Vditor - 浏览器端的 Markdown 编辑器](https://ld246.com/article/1549638745630)，同时也欢迎向我们提出建议或报告问题，谢谢 ❤️\n\n## 教程\n\n这是一篇讲解如何正确使用 **Markdown** 的排版示例，学会这个很有必要，能让你的文章有更佳清晰的排版。\n\n> 引用文本：Markdown is a text formatting syntax inspired\n\n## 语法指导\n\n### 普通内容\n\n这段内容展示了在内容里面一些排版格式，比如：\n\n- **加粗** - `**加粗**`\n- *倾斜* - `*倾斜*`\n- ~~删除线~~ - `~~删除线~~`\n- `Code 标记` - `` `Code 标记` ``\n- [超级链接](https://ld246.com) - `[超级链接](https://ld246.com)`\n- [username@gmail.com](mailto:username@gmail.com) - `[username@gmail.com](mailto:username@gmail.com)`\n\n### 提及用户\n\n@Vanessa 通过 `@User` 可以在内容中提及用户，被提及的用户将会收到系统通知。\n\n> NOTE:\n>\n> 1. @用户名之后需要有一个空格\n> 2. 新手没有艾特的功能权限\n\n### 表情符号 Emoji\n\n支持大部分标准的表情符号，可使用输入法直接输入，也可手动输入字符格式。通过输入 `:` 触发自动完成，可在个人设置中[设置常用表情](https://ld246.com/settings/function)。\n\n#### 一些表情例子\n\n😄 😆 😵 😭 😰 😅  😢 😤 😍 😌\n👍 👎 💯 👏 🔔 🎁 ❓ 💣 ❤️ ☕️ 🌀 🙇 💋 🙏 💢\n\n### 大标题 - Heading 3\n\n你可以选择使用 H1 至 H6，使用 ##(N) 打头。建议帖子或回帖中的顶级标题使用 Heading 3，不要使用 1 或 2，因为 1 是系统站点级，2 是帖子标题级。\n\n> NOTE: 别忘了 # 后面需要有空格！\n\n#### Heading 4\n\n##### Heading 5\n\n###### Heading 6\n\n### 图片\n\n```\n![alt 文本](http://image-path.png)\n![alt 文本](http://image-path.png \"图片 Title 值\")\n```\n\n支持复制粘贴直接上传。\n\n### 代码块\n\n#### 普通\n\n```\n*emphasize*    **strong**\n_emphasize_    __strong__\nvar a = 1\n```\n\n#### 语法高亮支持\n\n如果在 ``` 后面跟随语言名称，可以有语法高亮的效果哦，比如:\n\n##### 演示 Go 代码高亮\n\n```go\npackage main\n\nimport \"fmt\"\n\nfunc main() {\n	fmt.Println(\"Hello, 世界\")\n}\n```\n\n##### 演示 Java 高亮\n\n```java\npublic class HelloWorld {\n\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n\n}\n```\n\n> Tip: 语言名称支持下面这些: `ruby`, `python`, `js`, `html`, `erb`, `css`, `coffee`, `bash`, `json`, `yml`, `xml` ...\n\n### 有序、无序、任务列表\n\n#### 无序列表\n\n- Java\n  - Spring\n    - IoC\n    - AOP\n- Go\n  - gofmt\n  - Wide\n- Node.js\n  - Koa\n  - Express\n\n#### 有序列表\n\n1. Node.js\n   1. Express\n   2. Koa\n   3. Sails\n2. Go\n   1. gofmt\n   2. Wide\n3. Java\n   1. Latke\n   2. IDEA\n\n#### 任务列表\n\n- [X] 发布 Sym\n- [X] 发布 Solo\n- [ ] 预约牙医\n\n### 表格\n\n如果需要展示数据什么的，可以选择使用表格。\n\n| header 1 | header 2 |\n| -------- | -------- |\n| cell 1   | cell 2   |\n| cell 3   | cell 4   |\n| cell 5   | cell 6   |\n\n### 隐藏细节\n\n<details>\n<summary>这里是摘要部分。</summary>\n这里是细节部分。\n</details>\n\n### 段落\n\n空行可以将内容进行分段，便于阅读。（这是第一段）\n\n使用空行在 Markdown 排版中相当重要。（这是第二段）\n\n### 链接引用\n\n[链接文本][链接标识]\n\n[链接标识]: https://b3log.org\n```\n[链接文本][链接标识]\n\n[链接标识]: https://b3log.org\n```\n\n### 数学公式\n\n多行公式块：\n\n$$\n\\frac{1}{\n  \\Bigl(\\sqrt{\\phi \\sqrt{5}}-\\phi\\Bigr) e^{\n  \\frac25 \\pi}} = 1+\\frac{e^{-2\\pi}} {1+\\frac{e^{-4\\pi}} {\n    1+\\frac{e^{-6\\pi}}\n    {1+\\frac{e^{-8\\pi}}{1+\\cdots}}\n  }\n}\n$$\n\n行内公式：\n\n公式 $a^2 + b^2 = \\color{red}c^2$ 是行内。\n\n### 脑图\n\n```mindmap\n- 教程\n- 语法指导\n  - 普通内容\n  - 提及用户\n  - 表情符号 Emoji\n    - 一些表情例子\n  - 大标题 - Heading 3\n    - Heading 4\n      - Heading 5\n        - Heading 6\n  - 图片\n  - 代码块\n    - 普通\n    - 语法高亮支持\n      - 演示 Go 代码高亮\n      - 演示 Java 高亮\n  - 有序、无序、任务列表\n    - 无序列表\n    - 有序列表\n    - 任务列表\n  - 表格\n  - 隐藏细节\n  - 段落\n  - 链接引用\n  - 数学公式\n  - 脑图\n  - 流程图\n  - 时序图\n  - 甘特图\n  - 图表\n  - 五线谱\n  - Graphviz\n  - 多媒体\n  - 脚注\n- 快捷键\n```\n\n### 流程图\n\n```mermaid\ngraph TB\n    c1-->a2\n    subgraph one\n    a1-->a2\n    end\n    subgraph two\n    b1-->b2\n    end\n    subgraph three\n    c1-->c2\n    end\n```\n\n### 时序图\n\n```mermaid\nsequenceDiagram\n    Alice->>John: Hello John, how are you?\n    loop Every minute\n        John-->>Alice: Great!\n    end\n```\n\n### 甘特图\n\n```mermaid\ngantt\n    title A Gantt Diagram\n    dateFormat  YYYY-MM-DD\n    section Section\n    A task           :a1, 2019-01-01, 30d\n    Another task     :after a1  , 20d\n    section Another\n    Task in sec      :2019-01-12  , 12d\n    another task      : 24d\n```\n\n### 图表\n\n```echarts\n{\n  \"title\": { \"text\": \"最近 30 天\" },\n  \"tooltip\": { \"trigger\": \"axis\", \"axisPointer\": { \"lineStyle\": { \"width\": 0 } } },\n  \"legend\": { \"data\": [\"帖子\", \"用户\", \"回帖\"] },\n  \"xAxis\": [{\n      \"type\": \"category\",\n      \"boundaryGap\": false,\n      \"data\": [\"2019-05-08\",\"2019-05-09\",\"2019-05-10\",\"2019-05-11\",\"2019-05-12\",\"2019-05-13\",\"2019-05-14\",\"2019-05-15\",\"2019-05-16\",\"2019-05-17\",\"2019-05-18\",\"2019-05-19\",\"2019-05-20\",\"2019-05-21\",\"2019-05-22\",\"2019-05-23\",\"2019-05-24\",\"2019-05-25\",\"2019-05-26\",\"2019-05-27\",\"2019-05-28\",\"2019-05-29\",\"2019-05-30\",\"2019-05-31\",\"2019-06-01\",\"2019-06-02\",\"2019-06-03\",\"2019-06-04\",\"2019-06-05\",\"2019-06-06\",\"2019-06-07\"],\n      \"axisTick\": { \"show\": false },\n      \"axisLine\": { \"show\": false }\n  }],\n  \"yAxis\": [{ \"type\": \"value\", \"axisTick\": { \"show\": false }, \"axisLine\": { \"show\": false }, \"splitLine\": { \"lineStyle\": { \"color\": \"rgba(0, 0, 0, .38)\", \"type\": \"dashed\" } } }],\n  \"series\": [\n    {\n      \"name\": \"帖子\", \"type\": \"line\", \"smooth\": true, \"itemStyle\": { \"color\": \"#d23f31\" }, \"areaStyle\": { \"normal\": {} }, \"z\": 3,\n      \"data\": [\"18\",\"14\",\"22\",\"9\",\"7\",\"18\",\"10\",\"12\",\"13\",\"16\",\"6\",\"9\",\"15\",\"15\",\"12\",\"15\",\"8\",\"14\",\"9\",\"10\",\"29\",\"22\",\"14\",\"22\",\"9\",\"10\",\"15\",\"9\",\"9\",\"15\",\"0\"]\n    },\n    {\n      \"name\": \"用户\", \"type\": \"line\", \"smooth\": true, \"itemStyle\": { \"color\": \"#f1e05a\" }, \"areaStyle\": { \"normal\": {} }, \"z\": 2,\n      \"data\": [\"31\",\"33\",\"30\",\"23\",\"16\",\"29\",\"23\",\"37\",\"41\",\"29\",\"16\",\"13\",\"39\",\"23\",\"38\",\"136\",\"89\",\"35\",\"22\",\"50\",\"57\",\"47\",\"36\",\"59\",\"14\",\"23\",\"46\",\"44\",\"51\",\"43\",\"0\"]\n    },\n    {\n      \"name\": \"回帖\", \"type\": \"line\", \"smooth\": true, \"itemStyle\": { \"color\": \"#4285f4\" }, \"areaStyle\": { \"normal\": {} }, \"z\": 1,\n      \"data\": [\"35\",\"42\",\"73\",\"15\",\"43\",\"58\",\"55\",\"35\",\"46\",\"87\",\"36\",\"15\",\"44\",\"76\",\"130\",\"73\",\"50\",\"20\",\"21\",\"54\",\"48\",\"73\",\"60\",\"89\",\"26\",\"27\",\"70\",\"63\",\"55\",\"37\",\"0\"]\n    }\n  ]\n}\n```\n\n### 五线谱\n\n```abc\nX: 24\nT: Clouds Thicken\nC: Paul Rosen\nS: Copyright 2005, Paul Rosen\nM: 6/8\nL: 1/8\nQ: 3/8=116\nR: Creepy Jig\nK: Em\n|:\"Em\"EEE E2G|\"C7\"_B2A G2F|\"Em\"EEE E2G|\\\n\"C7\"_B2A \"B7\"=B3|\"Em\"EEE E2G|\n\"C7\"_B2A G2F|\"Em\"GFE \"D (Bm7)\"F2D|\\\n1\"Em\"E3-E3:|2\"Em\"E3-E2B|:\"Em\"e2e gfe|\n\"G\"g2ab3|\"Em\"gfeg2e|\"D\"fedB2A|\"Em\"e2e gfe|\\\n\"G\"g2ab3|\"Em\"gfe\"D\"f2d|\"Em\"e3-e3:|\n```\n\n### Graphviz\n\n```graphviz\ndigraph finite_state_machine {\n    rankdir=LR;\n    size=\"8,5\"\n    node [shape = doublecircle]; S;\n    node [shape = point ]; qi\n\n    node [shape = circle];\n    qi -> S;\n    S  -> q1 [ label = \"a\" ];\n    S  -> S  [ label = \"a\" ];\n    q1 -> S  [ label = \"a\" ];\n    q1 -> q2 [ label = \"ddb\" ];\n    q2 -> q1 [ label = \"b\" ];\n    q2 -> q2 [ label = \"b\" ];\n}\n```\n\n### Flowchart\n\n```flowchart\nst=>start: Start\nop=>operation: Your Operation\ncond=>condition: Yes or No?\ne=>end\n\nst->op->cond\ncond(yes)->e\ncond(no)->op\n```\n\n### 多媒体\n\n支持 v.qq.com，youtube.com，youku.com，coub.com，facebook.com/video，dailymotion.com，.mp4，.m4v，.ogg，.ogv，.webm，.mp3，.wav 链接解析\n\nhttps://v.qq.com/x/cover/zf2z0xpqcculhcz/y0016tj0qvh.html\n\n### 脚注\n\n这里是一个脚注引用[^1]，这里是另一个脚注引用[^bignote]。\n\n[^1]: 第一个脚注定义。\n    \n[^bignote]: 脚注定义可使用多段内容。\n    \n       缩进对齐的段落包含在这个脚注定义内。\n    \n       ```\n       可以使用代码块。\n       ```\n       还有其他行级排版语法，比如**加粗**和[链接](https://b3log.org)。\n    \n```\n这里是一个脚注引用[^1]，这里是另一个脚注引用[^bignote]。\n[^1]: 第一个脚注定义。\n[^bignote]: 脚注定义可使用多段内容。\n\n    缩进对齐的段落包含在这个脚注定义内。\n\n    ```\n    可以使用代码块。\n    ```\n\n    还有其他行级排版语法，比如**加粗**和[链接](https://b3log.org)。\n```\n\n## 快捷键\n\n我们的编辑器支持很多快捷键，具体请参考 [键盘快捷键](https://ld246.com/article/1474030007391)（或者按 \"`?` \"😼）\n', '0', 1, '2024-07-02 13:47:20', 1, '2024-12-12 10:13:46', '2024-12-12 10:13:47', 1, 'ceshi');
INSERT INTO `sys_notice` VALUES (1842148808657141762, '关于狸花猫', '1', '2', '2', '0', '#### 关于狸花猫\n\n狸花猫是一款基于 **SpringBoot** 和 **Vue** 的权限管理系统\n\n##### 为什么叫狸花猫\n\n家里养了两只狸花猫，想用它们作为系统的主题。用它们任何一只的名字命名都不太合适，干脆就按它们的品种来命名了。\n\n##### 系统功能\n\n系统包含完整的基于角色控制的 RBAC 权限管理系统，包括菜单管理、角色管理和用户管理。此外，还提供部门和岗位管理，适用于大多数业务场景，用户支持多部门并可指定默认部门，前后端均提供接口获取用户的默认信息。\n\n##### 字典管理\n\n系统字典支持普通字典和树形字典，并提供工具类用于获取和翻译字典信息，前端还提供 dict-tag 组件，可以通过字典 value 直接展示字典 label，并自动匹配 tag 样式。\n\n##### 通知公告\n\n通知公告集成了 Vditor 富文本解析器，并使用 SSE 实现了消息的实时发送与接收。\n\n##### 个人中心\n\n个人中心支持个性化系统主题配置，支持主题、布局、导航等页面设置。\n\n##### 系统设置\n\n管理员角色用户可以对系统进行进一步配置，包括默认密码设置、定期修改密码、同账号登录限制、自助注册配置、登录验证码开关、IP 黑名单和灰色模式。\n\n##### 其他功能\n\n系统还提供了日志服务、在线用户监控、缓存监控、服务监控以及定时任务等功能。\n', '1', 1, '2024-10-04 18:24:36', 1842151025015455745, '2024-10-04 20:39:31', '2024-10-04 20:39:30', NULL, NULL);

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务描述',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型编码',
  `type_msg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型描述',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名',
  `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名',
  `ip_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id（操作人）',
  `create_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人昵称',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间（操作时间）',
  `execute_time` int NULL DEFAULT NULL COMMENT '执行时长ms',
  `params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回值',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异常信息',
  `error_stack` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '堆栈信息',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求url',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理字符串，包含客户端操作系统、浏览器内核等信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除',
  `execute_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志执行状态',
  `cache_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户缓存key',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_operate_log
-- ----------------------------
INSERT INTO `sys_operate_log` VALUES (1897212055680385026, '清空操作日志', 'DELETE', '删除数据', 'com.lihua.controller.system.SysLogController', 'clearOperateLog', '0:0:0:0:0:0:0:1', 1, 'admin', 'admin', '2025-03-05 17:06:17', 9, NULL, '{\"code\":200,\"msg\":\"成功\",\"data\":null}', NULL, NULL, '/system/log/operate/clear', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36 Edg/133.0.0.0', '0', '0', 'REDIS_CACHE_LOGIN_USER:1:1741165503793:62ed67b3c31747f8bde99220b163f1c1');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` bigint NOT NULL COMMENT ' 主键',
  `dept_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门主键',
  `dept_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门编码',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编码',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `manager` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `fax` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '传真',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统单位/岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1842129333329264642, '1810226204790657025', 'lihua', '研发岗', 'lihua_dev', 1, '0', '', NULL, NULL, NULL, '0', 1, '2024-10-04 17:07:13', 1, '2024-10-04 17:07:46', NULL);
INSERT INTO `sys_post` VALUES (1842129449343713282, '1810226204790657025', 'lihua', '销售岗', 'lihua_sell', 2, '0', NULL, NULL, NULL, NULL, '0', 1, '2024-10-04 17:07:40', NULL, NULL, NULL);
INSERT INTO `sys_post` VALUES (1842188089392041986, '1810226204790657025', 'lihua', '测试岗', 'lihua_test', 3, '0', NULL, NULL, NULL, NULL, '0', 1, '2024-10-04 21:00:41', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色编码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ROLE_admin', '0', '0', NULL, '2024-05-16 21:32:55', 1, '2024-06-19 22:49:48', NULL);
INSERT INTO `sys_role` VALUES (1842149851067514881, '访客用户', 'ROLE_visitor', '0', '0', 1, '2024-10-04 18:28:45', 1, '2025-03-05 16:58:52', '');
INSERT INTO `sys_role` VALUES (1861768418410799106, '角色测试', 'ROLE_TEST', '0', '0', 1, '2024-11-27 21:45:56', 1, '2025-03-05 16:59:01', NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1775035631645659138);
INSERT INTO `sys_role_menu` VALUES (1, 1776948212783730690);
INSERT INTO `sys_role_menu` VALUES (1, 1777536058626834433);
INSERT INTO `sys_role_menu` VALUES (1, 1777536311941824513);
INSERT INTO `sys_role_menu` VALUES (1, 1777536895235293186);
INSERT INTO `sys_role_menu` VALUES (1, 1777538040162844673);
INSERT INTO `sys_role_menu` VALUES (1, 1777538768721838082);
INSERT INTO `sys_role_menu` VALUES (1, 1777539832263114753);
INSERT INTO `sys_role_menu` VALUES (1, 1784084466574819330);
INSERT INTO `sys_role_menu` VALUES (1, 1897209379781541890);
INSERT INTO `sys_role_menu` VALUES (1, 1897209872981360641);
INSERT INTO `sys_role_menu` VALUES (1778051355835736065, 1775035631645659138);
INSERT INTO `sys_role_menu` VALUES (1778051355835736065, 1776948212783730690);
INSERT INTO `sys_role_menu` VALUES (1778051355835736065, 1777536058626834433);
INSERT INTO `sys_role_menu` VALUES (1778051355835736065, 1777536311941824513);
INSERT INTO `sys_role_menu` VALUES (1778051355835736065, 1777536895235293186);
INSERT INTO `sys_role_menu` VALUES (1778051355835736065, 1897209379781541890);
INSERT INTO `sys_role_menu` VALUES (1778051355835736065, 1897209872981360641);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1775035631645659138);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1776948212783730690);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1777536058626834433);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1777536311941824513);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1777536895235293186);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1777538040162844673);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1777538768721838082);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1777539832263114753);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1784084466574819330);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1823518320191033346);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1838036266821033985);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1838487275271753729);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1838581685913554946);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1840684744634474498);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1841350424845307905);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1842179763459522561);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1842179829247180801);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1842179926320152578);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1842179977343860738);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1842180022327771137);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1842180212346519554);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1842180329170468866);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1866101773239513090);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1866102220608172033);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1869722347316580354);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870098893441273858);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870290012938657794);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870301196588900353);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870319939100565506);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870342042247979009);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870463250452930562);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870661733822672897);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870738350989291522);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1870752914480996354);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1892898089370066945);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1892898801227341825);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1897209379781541890);
INSERT INTO `sys_role_menu` VALUES (1842149851067514881, 1897209872981360641);

-- ----------------------------
-- Table structure for sys_setting
-- ----------------------------
DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting`  (
  `setting_component_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设置组件名称',
  `setting_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设置名称',
  `setting_json` varbinary(2000) NULL DEFAULT NULL COMMENT '设置参数',
  PRIMARY KEY (`setting_component_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和登陆后设置关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_setting
-- ----------------------------
INSERT INTO `sys_setting` VALUES ('GrayModelSetting', '灰色模式', 0x7B22656E61626C65223A66616C73657D);
INSERT INTO `sys_setting` VALUES ('IntervalUpdatePasswordSetting', '定期修改密码', 0x7B22656E61626C65223A66616C73652C22756E6974223A226D6F6E7468227D);
INSERT INTO `sys_setting` VALUES ('RestrictAccessIpSetting', '限制访问IP', 0x7B22656E61626C65223A66616C73652C2269704C697374223A5B22225D7D);
INSERT INTO `sys_setting` VALUES ('SameAccountLoginSetting', '同账号登录限制', 0x7B22656E61626C65223A66616C73652C226D6178696D756D223A317D);
INSERT INTO `sys_setting` VALUES ('SignInSetting', '自助注册', 0x7B22656E61626C65223A747275652C2264657074496473223A5B2231383130323236323034373930363537303235222C2231383432313238343039363030393137353035225D2C2264656661756C74446570744964223A6E756C6C2C22706F7374496473223A5B2231383432313838303839333932303431393836225D2C22726F6C65496473223A5B2231383432313439383531303637353134383831225D7D);
INSERT INTO `sys_setting` VALUES ('VerificationCodeSetting', '登录验证码', 0x7B22656E61626C65223A747275657D);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户状态',
  `theme` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户系统主题json',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `password_update_time` datetime NULL DEFAULT NULL COMMENT '密码更新时间',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `register_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册类型 0 管理员新增，1 用户自助注册',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_user_username`(`username` ASC) USING BTREE,
  INDEX `idx_sys_user_email`(`email` ASC) USING BTREE,
  INDEX `idx_sys_user_phone_number`(`phone_number` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$7uob8wEDL7VvXmC9wC4MZ.7Ut3qJOaiFVErYgpsbhAWxSA8jZWL9u', 'admin', '{\"value\":\"XiaoMiaoCool\",\"type\":\"icon\",\"backgroundColor\":\"conic-gradient(from 45deg, rgb(22, 119, 255),rgb(245, 34, 45),rgb(250, 84, 28),rgb(250, 173, 20),rgb(19, 194, 194),rgb(82, 196, 26),rgb(47, 84, 235),rgb(114, 46, 209))\"}', '1', '0', '{\"layoutType\":\"header-sider\",\"componentSize\":\"default\",\"showViewTabs\":true,\"isDarkTheme\":false,\"colorPrimary\":\"rgb(114, 46, 209)\",\"antColorPrimary\":\"#722ed1\",\"siderTheme\":\"light\",\"groundGlass\":true,\"affixHead\":true,\"layoutBackgroundColor\":\"rgba(255,255,255,0.6)\",\"siderBackgroundColor\":\"rgba(255,255,255,1)\",\"siderMode\":\"inline\",\"siderGroup\":false,\"siderWith\":220,\"originSiderWith\":220,\"routeTransition\":\"zoom\",\"grayModel\":\"none\",\"themeConfig\":{\"token\":{\"colorPrimary\":\"rgb(114, 46, 209)\"}}}', '0', NULL, '2024-06-02 16:57:25', 1, '2024-12-22 19:13:40', '2024-10-04 20:59:29', '2651518588@qq.com', '15510916240', NULL, '0');

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '绑定时间',
  `create_id` bigint NULL DEFAULT NULL COMMENT '绑定人id',
  `default_dept` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认单位',
  PRIMARY KEY (`user_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES (1, 1842128590274756609, '2024-12-01 13:36:09', 1, '0');

-- ----------------------------
-- Table structure for sys_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_notice`;
CREATE TABLE `sys_user_notice`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `notice_id` bigint NOT NULL COMMENT '公告id',
  `star_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'star标记',
  `read_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '已读标记',
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  PRIMARY KEY (`user_id`, `notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_notice
-- ----------------------------
INSERT INTO `sys_user_notice` VALUES (1, 1, '1', '1', '2024-12-19 22:01:03');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `post_id` bigint NOT NULL COMMENT '岗位id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '绑定时间',
  `create_id` bigint NULL DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1, '2024-06-02 16:57:25', 1);
INSERT INTO `sys_user_post` VALUES (1, 3, '2024-06-02 16:57:25', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '绑定时间',
  `create_id` bigint NULL DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, '2024-06-02 16:57:25', 1);

-- ----------------------------
-- Table structure for sys_view_tab
-- ----------------------------
DROP TABLE IF EXISTS `sys_view_tab`;
CREATE TABLE `sys_view_tab`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `affix` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否固定（1固定，0不固定）',
  `star` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否收藏（1收藏，0不收藏）',
  PRIMARY KEY (`user_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户菜单收藏管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_view_tab
-- ----------------------------
INSERT INTO `sys_view_tab` VALUES (1, 1777536311941824513, '0', '1');
INSERT INTO `sys_view_tab` VALUES (1, 1897209379781541890, '1', '1');

SET FOREIGN_KEY_CHECKS = 1;
