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

 Date: 18/08/2024 17:57:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 主键',
  `parent_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父级id',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编码',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `manager` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `fax` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '传真',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统单位/岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1810226204790657025', '0', '小米科技', 'xiaomi', 1, '0', '雷军儿', NULL, NULL, NULL, '0', '1', '2024-07-08 16:15:34', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES ('1810226204790657026', '1810226204790657025', '紫米科技', 'zimi', 2, '0', '雷军二', NULL, NULL, NULL, '0', '1', '2024-07-08 16:15:34', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID',
  `parent_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父级字典id',
  `dict_type_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型编码',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典标签',
  `value` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后一次更新时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `tag_style` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签的样式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES ('1', '0', '1', '是', '0', 2, '系统选项是', '0', NULL, NULL, '1', '2024-03-14 13:31:41', '1', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768153329837694977', '0', '1766758645678043137', '树形字典', 'tree', 1, NULL, '0', '1', '2024-03-14 13:52:59', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768153331267952642', '0', '1766758645678043137', '一般字典', 'default', 2, NULL, '0', '1', '2024-03-14 13:53:00', '1', '2024-03-14 13:59:21', '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768169289755815938', '1768153332689821697', '1766758645678043137', '其他2', 'o1', 1, NULL, '0', '1', '2024-03-14 14:56:24', '1', '2024-03-14 15:27:38', '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768178926144090113', '1768169289755815938', '1766758645678043137', 'qita3', 'o3', 1, NULL, '0', '1', '2024-03-14 15:34:42', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768445908919676929', '1768153332689821697', '1766758645678043137', '123', '312', 2, NULL, '0', '1', '2024-03-15 09:15:35', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768446977636724738', '1768153332689821697', '1766758645678043137', '666', '666', 3, NULL, '0', '1', '2024-03-15 09:19:50', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768511746280398850', '1768153331267952642', '1766758645678043137', '1', '1', 1, NULL, '0', '1', '2024-03-15 13:37:12', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768512214658326529', '1768153331267952642', '1766758645678043137', '2', '2', 2, NULL, '0', '1', '2024-03-15 13:39:04', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768512477536329729', '1768153331267952642', '1766758645678043137', '3', '3', 3, NULL, '0', '1', '2024-03-15 13:40:07', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768514109091225601', '1768153331267952642', '1766758645678043137', '4', '4', 4, NULL, '0', '1', '2024-03-15 13:46:36', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768521531268669442', '1768514109091225601', '1766758645678043137', '5', '5', 1, NULL, '0', '1', '2024-03-15 14:16:05', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768521752086192130', '1768153331267952642', '1766758645678043137', '5', '5', 5, NULL, '0', '1', '2024-03-15 14:16:58', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768521971699949570', '1768153331267952642', '1766758645678043137', '6', '6', 6, NULL, '0', '1', '2024-03-15 14:17:50', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768522081397776385', '1768153331267952642', '1766758645678043137', '7', '7', 7, NULL, '0', '1', '2024-03-15 14:18:16', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768522274612584449', '1768153331267952642', '1766758645678043137', '8', '8', 8, NULL, '0', '1', '2024-03-15 14:19:02', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768522468615921666', '1768153331267952642', '1766758645678043137', '9', '9', 90, NULL, '0', '1', '2024-03-15 14:19:49', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768523041901780994', '1768153331267952642', '1766758645678043137', '10', '10', 913, NULL, '0', '1', '2024-03-15 14:22:05', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768523105671979010', '1768153331267952642', '1766758645678043137', '11', '11', 9141, NULL, '0', '1', '2024-03-15 14:22:21', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768523826605727745', '1768153331267952642', '1766758645678043137', '12', '12', 9142, NULL, '0', '1', '2024-03-15 14:25:13', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768524286381137922', '1768153329837694977', '1766758645678043137', '1', '1', 1, NULL, '0', '1', '2024-03-15 14:27:02', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768524400394903553', '1768153329837694977', '1766758645678043137', '2', '2', 2, NULL, '0', '1', '2024-03-15 14:27:29', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768524459643641857', '1768153329837694977', '1766758645678043137', '3', '3', 3, NULL, '0', '1', '2024-03-15 14:27:43', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768524632725790722', '1768153329837694977', '1766758645678043137', '4', '4', 4, NULL, '0', '1', '2024-03-15 14:28:25', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768525031620878338', '1768153329837694977', '1766758645678043137', '5', '5', 55, NULL, '0', '1', '2024-03-15 14:30:00', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768525148650348545', '1768153329837694977', '1766758645678043137', '7', '7', 56, NULL, '0', '1', '2024-03-15 14:30:28', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768525278619246593', '1768153329837694977', '1766758645678043137', '123', '123', 57, NULL, '0', '1', '2024-03-15 14:30:59', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768525360294928385', '1768153329837694977', '1766758645678043137', '13123', '123123', 58, NULL, '0', '1', '2024-03-15 14:31:18', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768525473704714241', '1768153329837694977', '1766758645678043137', '44', '44', 59, NULL, '0', '1', '2024-03-15 14:31:45', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768525621910446081', '1768153329837694977', '1766758645678043137', '1', '1', 602, NULL, '0', '1', '2024-03-15 14:32:21', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` VALUES ('1768553675202408450', '1768527062662258690', '1766758645678043137', '4', '4', 3, NULL, '0', '1', '2024-03-15 16:23:49', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` VALUES ('1770356149437501442', '0', 'yes-no', '测试', 'test', 1, NULL, '0', '1', '2024-03-20 15:46:12', '1', '2024-03-20 16:00:47', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1771163317281083393', '0', 'sys_status', '正常', '0', 1, NULL, '0', '1', '2024-03-22 21:13:36', '1', '2024-03-24 13:42:09', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1771163394133315585', '0', 'sys_status', '停用', '1', 2, NULL, '0', '1', '2024-03-22 21:13:54', '1', '2024-03-22 21:18:01', '0', 'error');
INSERT INTO `sys_dict_data` VALUES ('1771165766595235841', '0', 'sys_dict_type', '一般字典', '0', 1, NULL, '0', '1', '2024-03-22 21:23:20', '1', '2024-03-24 20:46:11', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1771165768948240385', '0', 'sys_dict_type', '树型字典', '1', 2, NULL, '0', '1', '2024-03-22 21:23:21', '1', '2024-03-24 20:46:12', '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1771166154488664065', '0', 'sys_dict_tag_style', '默认', 'default', 1, NULL, '0', '1', '2024-03-22 21:24:53', '1', '2024-07-05 15:25:08', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1771166156803919874', '0', 'sys_dict_tag_style', '主要', 'processing', 2, NULL, '0', '1', '2024-03-22 21:24:53', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1771166159454720001', '0', 'sys_dict_tag_style', '成功', 'success', 3, NULL, '0', '1', '2024-03-22 21:24:54', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1771166162894049282', '0', 'sys_dict_tag_style', '警告', 'warning', 4, NULL, '0', '1', '2024-03-22 21:24:55', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` VALUES ('1771166168740909057', '0', 'sys_dict_tag_style', '错误', 'error', 5, NULL, '0', '1', '2024-03-22 21:24:56', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` VALUES ('1772463094826942465', '0', 'sys_dict_tag_style', '红色', '#ff4d4f', 7, NULL, '0', '1', '2024-03-26 11:18:27', '1', '2024-07-21 20:57:03', '0', '#ff4d4f');
INSERT INTO `sys_dict_data` VALUES ('1772466547863207937', '0', 'sys_language', '中文', 'cn', 1, NULL, '0', '1', '2024-03-26 11:32:11', '1', '2024-03-27 15:04:17', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1772466549687730178', '0', 'sys_language', '英语', 'en', 2, NULL, '0', '1', '2024-03-26 11:32:11', '1', '2024-03-27 15:04:20', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1773701158649315330', '0', 'sys_menu_type', '目录', 'directory', 1, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-03-30 22:40:29', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1773701160050212865', '0', 'sys_menu_type', '页面', 'page', 2, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-04-05 11:07:54', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1773701161455304706', '0', 'sys_menu_type', '权限', 'perms', 4, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-04-07 20:17:27', '0', 'warning');
INSERT INTO `sys_dict_data` VALUES ('1774046926568538113', '0', 'sys_menu_type', '链接', 'link', 3, NULL, '0', '1', '2024-03-30 20:12:02', '1', '2024-04-05 11:08:03', '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1774250426447544321', '0', 'sys_link_menu_open_type', '系统内', 'inner', 1, NULL, '0', '1', '2024-03-31 09:40:40', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774250428016214017', '0', 'sys_link_menu_open_type', '新页面', 'new-page', 2, NULL, '0', '1', '2024-03-31 09:40:41', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774252801971306497', '0', 'sys_whether', '是', '0', 1, NULL, '0', '1', '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774252803737108481', '0', 'sys_whether', '否', '1', 2, NULL, '0', '1', '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1780864938674667522', '0', 'sys_dept_type', '部门', 'dept', 1, NULL, '0', '1', '2024-04-18 15:44:23', '1', '2024-04-20 21:35:37', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1780864940843122690', '0', 'sys_dept_type', '岗位', 'post', 2, NULL, '0', '1', '2024-04-18 15:44:23', '1', '2024-04-20 21:35:37', '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1794263233540739074', '0', 'user_gender', '女', '0', 2, NULL, '0', '1', '2024-05-25 15:04:25', '1', '2024-05-25 15:08:41', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1794263235541422081', '0', 'user_gender', '男', '1', 1, NULL, '0', '1', '2024-05-25 15:04:26', '1', '2024-05-25 15:08:41', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1794263238364188674', '0', 'user_gender', '未知', '2', 3, NULL, '0', '1', '2024-05-25 15:04:26', '1', '2024-06-05 21:32:38', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1814191392971739137', '0', 'sys_notice_status', '未发布', '0', 1, NULL, '0', '1', '2024-07-19 14:51:49', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1814191395400241153', '0', 'sys_notice_status', '已发布', '1', 2, NULL, '0', '1', '2024-07-19 14:51:49', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1814191397560307713', '0', 'sys_notice_status', '已撤销', '2', 3, NULL, '0', '1', '2024-07-19 14:51:50', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` VALUES ('1814191691274833922', '0', 'sys_notice_type', '通知', '0', 1, NULL, '0', '1', '2024-07-19 14:53:00', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1814191692868669441', '0', 'sys_notice_type', '公告', '1', 2, NULL, '0', '1', '2024-07-19 14:53:00', '1', '2024-07-20 17:53:15', '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1814602689115856897', '0', 'sys_notice_user_scope', '全部用户', '0', 1, NULL, '0', '1', '2024-07-20 18:06:09', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1814602691913457666', '0', 'sys_notice_user_scope', '指定用户', '1', 2, NULL, '0', '1', '2024-07-20 18:06:10', '1', '2024-07-21 22:04:21', '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1814603526915497986', '0', 'sys_notice_priority', '紧急', '0', 1, NULL, '0', '1', '2024-07-20 18:09:29', '1', '2024-07-21 20:59:43', '0', '#ff4d4f');
INSERT INTO `sys_dict_data` VALUES ('1814603528811323393', '0', 'sys_notice_priority', '高', '1', 2, NULL, '0', '1', '2024-07-20 18:09:30', '1', '2024-07-21 20:59:44', '0', '#faad14');
INSERT INTO `sys_dict_data` VALUES ('1814603531445346306', '0', 'sys_notice_priority', '中', '2', 3, NULL, '0', '1', '2024-07-20 18:09:30', '1', '2024-07-21 20:59:44', '0', '#1677ff');
INSERT INTO `sys_dict_data` VALUES ('1814603537514504193', '0', 'sys_notice_priority', '低', '3', 4, NULL, '0', '1', '2024-07-20 18:09:32', '1', '2024-07-21 20:59:45', '0', '#52c41a');
INSERT INTO `sys_dict_data` VALUES ('1815008261946531841', '0', 'sys_dict_tag_style', '橙色', '#faad14', 8, NULL, '0', '1', '2024-07-21 20:57:46', NULL, NULL, '0', '#faad14');
INSERT INTO `sys_dict_data` VALUES ('1815008480134225921', '0', 'sys_dict_tag_style', '蓝色', '#1677ff', 9, NULL, '0', '1', '2024-07-21 20:58:38', NULL, NULL, '0', '#1677ff');
INSERT INTO `sys_dict_data` VALUES ('1815008645746319361', '0', 'sys_dict_tag_style', '绿色', '#52c41a', 10, NULL, '0', '1', '2024-07-21 20:59:17', NULL, NULL, '0', '#52c41a');
INSERT INTO `sys_dict_data` VALUES ('1823522986106695682', '0', 'sys_log_status', '成功', '0', 1, NULL, '0', '1', '2024-08-14 08:52:14', '1', '2024-08-14 08:52:43', '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1823522988254179330', '0', 'sys_log_status', '失败', '1', 2, NULL, '0', '1', '2024-08-14 08:52:14', '1', '2024-08-14 08:52:43', '0', 'error');
INSERT INTO `sys_dict_data` VALUES ('2', '0', '1', '否', '1', 1, '系统选项2否', '0', NULL, NULL, '1', '2024-03-15 16:30:56', '0', NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型名称',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型编码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典备注',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最后一次更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('1771163166122561537', '系统状态', 'sys_status', '0', '系统通用状态标识', '1', '2024-03-22 21:13:00', '1', '2024-07-19 15:27:12', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1771164641267666946', '字典标签样式', 'sys_dict_tag_style', '0', '字典配置配置中，样式列的字典', '1', '2024-03-22 21:18:52', '1', '2024-03-22 21:20:00', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1771165529122131969', '字典类型', 'sys_dict_type', '0', '区分字典为一般字典还是树型字典', '1', '2024-03-22 21:22:23', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1772466287761833985', '系统语言', 'sys_language', '0', '系统国际化语言配置', '1', '2024-03-26 11:31:08', '1', '2024-03-26 11:32:50', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1773700957867982850', '菜单类型', 'sys_menu_type', '0', '系统菜单配置类型，分为 目录、页面、链接、权限', '1', '2024-03-29 21:17:17', '1', '2024-04-01 09:59:35', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1774249964684034050', '链接菜单打开方式', 'sys_link_menu_open_type', '0', '链接菜单打开方式，分为系统内嵌套和浏览器新页面', '1', '2024-03-31 09:38:50', '1', '2024-03-31 09:41:45', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1774252683993923586', '系统是否', 'sys_whether', '0', '系统是否选项字典', '1', '2024-03-31 09:49:39', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1780864852875984898', '部门类型', 'sys_dept_type', '0', '保存部门操作时的类型选项，分为部门和岗位', '1', '2024-04-18 15:44:02', '1', '2024-04-20 21:35:36', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1794262937292853250', '用户性别', 'user_gender', '0', '系统用户性别字典', '1', '2024-05-25 15:03:15', '1', '2024-05-25 15:03:26', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1814191109734584322', '公告状态', 'sys_notice_status', '0', '系统公告状态字典', '1', '2024-07-19 14:50:41', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1814191516905033729', '公告类型', 'sys_notice_type', '0', '系统公告类型字典', '1', '2024-07-19 14:52:18', '1', '2024-07-19 14:52:32', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1814602561218945026', '用户范围', 'sys_notice_user_scope', '0', '通知公告接收消息的用户范围', '1', '2024-07-20 18:05:39', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1814603011422953473', '优先级别', 'sys_notice_priority', '0', '通知公告优先程度', '1', '2024-07-20 18:07:26', '1', '2024-07-29 22:13:21', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1823522921661214721', '日志执行结果', 'sys_log_status', '0', '日志记录程序执行是否成功', '1', '2024-08-14 08:51:59', NULL, NULL, '0', '0');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务描述',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型编码',
  `type_msg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型描述',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名',
  `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名',
  `ip_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `create_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人昵称',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id（操作人）',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES ('1824989192840216577', '用戶登录', 'LOGIN', '登录', 'com.lihua.system.controller.SysAuthenticationController', 'login', '0:0:0:0:0:0:0:1', 'Yukino', 'admin', '1', '2024-08-18 09:58:25', 1109, '{\"currentUser\":\"{\\\"id\\\":null,\\\"username\\\":\\\"admin\\\",\\\"nickname\\\":null,\\\"avatar\\\":null,\\\"gender\\\":null,\\\"status\\\":null,\\\"theme\\\":null,\\\"phoneNumber\\\":null,\\\"email\\\":null,\\\"remark\\\":null}\",\"captchaVerification\":\"\\\"offCaptcha\\\"\"}', NULL, NULL, NULL, '/system/login', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_login_log` VALUES ('1825006224629551105', '用戶登录', 'LOGIN', '登录', 'com.lihua.system.controller.SysAuthenticationController', 'login', '0:0:0:0:0:0:0:1', 'Yukino', 'admin', '1', '2024-08-18 11:06:06', 1166, '{\"currentUser\":\"{\\\"id\\\":null,\\\"username\\\":\\\"admin\\\",\\\"nickname\\\":null,\\\"avatar\\\":null,\\\"gender\\\":null,\\\"status\\\":null,\\\"theme\\\":null,\\\"phoneNumber\\\":null,\\\"email\\\":null,\\\"remark\\\":null}\",\"captchaVerification\":\"\\\"offCaptcha\\\"\"}', NULL, NULL, NULL, '/system/login', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_login_log` VALUES ('1825035074105233409', '用戶登录', 'LOGIN', '登录', 'com.lihua.system.controller.SysAuthenticationController', 'login', '0:0:0:0:0:0:0:1', 'Yukino', 'admin', '1', '2024-08-18 13:00:44', 80, '{\"currentUser\":\"{\\\"id\\\":null,\\\"username\\\":\\\"admin\\\",\\\"nickname\\\":null,\\\"avatar\\\":null,\\\"gender\\\":null,\\\"status\\\":null,\\\"theme\\\":null,\\\"phoneNumber\\\":null,\\\"email\\\":null,\\\"remark\\\":null}\",\"captchaVerification\":\"\\\"offCaptcha\\\"\"}', NULL, NULL, NULL, '/system/login', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_login_log` VALUES ('1825093874866704386', '用戶登录', 'LOGIN', '登录', 'com.lihua.system.controller.SysAuthenticationController', 'login', '0:0:0:0:0:0:0:1', 'Yukino', 'admin', '1', '2024-08-18 16:54:23', 113, '{\"currentUser\":\"{\\\"id\\\":null,\\\"username\\\":\\\"admin\\\",\\\"nickname\\\":null,\\\"avatar\\\":null,\\\"gender\\\":null,\\\"status\\\":null,\\\"theme\\\":null,\\\"phoneNumber\\\":null,\\\"email\\\":null,\\\"remark\\\":null}\",\"captchaVerification\":\"\\\"offCaptcha\\\"\"}', NULL, NULL, NULL, '/system/login', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_login_log` VALUES ('1825107352520216578', '用戶登录', 'LOGIN', '登录', 'com.lihua.system.controller.SysAuthenticationController', 'login', '0:0:0:0:0:0:0:1', 'Yukino', 'admin', '1', '2024-08-18 17:47:56', 75, '{\"currentUser\":\"{\\\"id\\\":null,\\\"username\\\":\\\"admin\\\",\\\"nickname\\\":null,\\\"avatar\\\":null,\\\"gender\\\":null,\\\"status\\\":null,\\\"theme\\\":null,\\\"phoneNumber\\\":null,\\\"email\\\":null,\\\"remark\\\":null}\",\"captchaVerification\":\"\\\"offCaptcha\\\"\"}', NULL, NULL, NULL, '/system/login', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');

-- ----------------------------
-- Table structure for sys_login_setting
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_setting`;
CREATE TABLE `sys_login_setting`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `component_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设置描述',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户登录后设置管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_setting
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
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
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
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
INSERT INTO `sys_menu` VALUES ('031f293f02c84e4d9e27f866e18bc019', '1775035631645659138', '菜单管理', '菜单管理', 'page', '/menu', '/system/menu/SystemMenu.vue', '0', '0', 'page', 'BarsOutlined', 3, NULL, NULL, '1', '2024-06-02 12:23:14', '0', NULL, '0', '111', NULL, '0', 'new-page');
INSERT INTO `sys_menu` VALUES ('031f293f02c84e4d9e27f866e18bc059', '1775035631645659138', '字典管理', '字典管理', 'page', '/dict', '/system/dict/SystemDict.vue', '0', '0', 'page', 'ReadOutlined', 6, NULL, NULL, '1', '2024-06-19 20:30:07', '0', NULL, '0', '22222', '{\"name\": \"John\"}', '0', 'new-page');
INSERT INTO `sys_menu` VALUES ('1775035631645659138', '0', '系统管理', '系统管理', 'directory', '/system', NULL, '0', '0', 'directory', 'SettingOutlined', 2, '1', '2024-04-02 13:40:48', '1', '2024-06-19 22:49:12', '0', NULL, '1', NULL, NULL, '0', 'new-page');
INSERT INTO `sys_menu` VALUES ('1775365169634258945', '0', '外部链接', '外部链接', 'directory', '/link', NULL, '0', '1', 'directory', 'NodeIndexOutlined', 4, '1', '2024-04-03 11:30:16', '1', '2024-08-14 08:32:20', '0', NULL, '1', NULL, NULL, '0', 'new-page');
INSERT INTO `sys_menu` VALUES ('1775365569678585857', '1775365169634258945', '系统内显示', '系统内显示', 'link', '/inner', NULL, '0', '1', 'link', 'ChromeOutlined', 1, '1', '2024-04-03 11:31:51', '1', '2024-08-14 08:32:20', '0', NULL, '1', 'http://boot3.jeecg.com/dashboard/analysis', NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1776075821136220161', '1775365169634258945', '系统外显示', NULL, 'link', '/sys-out', NULL, '0', '1', NULL, 'ChromeOutlined', 2, '1', '2024-04-05 10:34:08', '1', '2024-08-14 08:32:21', '0', NULL, '1', 'https://gitee.com/yukino_git/lihua', NULL, '0', 'new-page');
INSERT INTO `sys_menu` VALUES ('1776946902768373762', '031f293f02c84e4d9e27f866e18bc019', '列表查询', NULL, 'perms', NULL, NULL, '0', '0', 'system:menu:list', NULL, 1, '1', '2024-04-07 20:15:30', '1', '2024-06-19 21:55:09', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1776947169823903746', '031f293f02c84e4d9e27f866e18bc019', '菜单保存', NULL, 'perms', '', NULL, '0', '0', 'system:menu:save', NULL, 2, '1', '2024-04-07 20:16:34', '1', '2024-06-19 21:55:08', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1776947236303622146', '031f293f02c84e4d9e27f866e18bc019', '菜单删除', NULL, 'perms', NULL, NULL, '0', '0', 'system:menu:delete', NULL, 3, '1', '2024-04-07 20:16:50', '1', '2024-06-19 21:55:08', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1776948046425051138', '031f293f02c84e4d9e27f866e18bc059', '列表查询', NULL, 'perms', NULL, NULL, '0', '0', 'system:dict:list', NULL, 1, '1', '2024-04-07 20:20:03', '1', '2024-06-19 21:55:07', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1776948212783730690', '031f293f02c84e4d9e27f866e18bc059', '字典保存', NULL, 'perms', '', NULL, '0', '0', 'system:dict:save', NULL, 2, '1', '2024-04-07 20:20:43', '1', '2024-06-19 21:55:06', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1776948371953373186', '031f293f02c84e4d9e27f866e18bc059', '字典删除', NULL, 'perms', '', NULL, '0', '0', 'system:dict:delete', NULL, 3, '1', '2024-04-07 20:21:21', '1', '2024-06-19 21:55:05', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1776948618620391426', '031f293f02c84e4d9e27f866e18bc059', '字典配置', NULL, 'perms', NULL, NULL, '0', '0', 'system:dict:config', NULL, 4, '1', '2024-04-07 20:22:19', '1', '2024-06-19 21:55:05', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1777536058626834433', '1775035631645659138', '角色管理', '角色管理', 'page', '/role', '/system/role/SystemRole.vue', '0', '0', 'page', 'TeamOutlined', 2, '1', '2024-04-09 11:16:36', '1', '2024-06-02 12:23:03', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1777536311941824513', '1775035631645659138', '用户管理', '用户管理', 'page', '/user', '/system/user/SystemUser.vue', '0', '0', 'page', 'UserSwitchOutlined', 1, '1', '2024-04-09 11:17:36', '1', '2024-06-02 12:22:57', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1777536895235293186', '1775035631645659138', '部门管理', '部门管理', 'page', '/dept', '/system/dept/SystemDept.vue', '0', '0', 'page', 'ApartmentOutlined', 4, '1', '2024-04-09 11:19:56', '1', '2024-06-02 12:23:50', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1777538040162844673', '0', '日志管理', '日志管理', 'directory', '/log', NULL, '0', '0', 'directory', 'FileSearchOutlined', 3, '1', '2024-04-09 11:24:28', '1', '2024-08-14 15:38:36', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1777538768721838082', '1777538040162844673', '登录日志', '登录日志', 'page', '/login', '/system/log/SystemLoginLog.vue', '0', '0', 'page', 'FileProtectOutlined', 1, '1', '2024-04-09 11:27:22', '1', '2024-08-14 15:43:44', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1777539419447132162', '1777538040162844673', '操作日志', '操作日志', 'page', '/operate', '/log/operate/LogOperate.vue', '0', '1', 'page', 'FileSyncOutlined', 2, '1', '2024-04-09 11:29:57', '1', '2024-08-14 08:32:26', '1', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1777539832263114753', '1775035631645659138', '通知公告', '通知公告', 'page', '/system/notice', '/system/notice/SystemNotice.vue', '0', '0', 'page', 'MessageOutlined', 7, '1', '2024-04-09 11:31:36', '1', '2024-07-10 21:14:30', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1780421335728918529', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:21:40', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780422393649885185', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:25:52', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780422522222088193', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:26:23', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780422633165615105', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:26:49', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780422872471621633', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:27:46', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780422881095110657', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:27:48', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780423014360743938', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:28:20', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780423917344051201', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:31:55', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1780427329154502657', NULL, 'admin', 'admin', 'page', NULL, NULL, NULL, NULL, 'page', NULL, NULL, '1', '2024-04-17 10:45:29', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1784084466574819330', '1775035631645659138', '岗位管理', '岗位管理', 'page', '/post', '/system/post/SystemPost.vue', '0', '0', 'page', 'ScheduleOutlined', 5, '1', '2024-04-27 12:57:38', '1', '2024-06-16 18:46:09', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1802295407218626562', '0', '多层级测试', '多层级测试', 'directory', '/t', NULL, '0', '1', 'directory', NULL, 5, '1', '2024-06-16 19:01:25', '1', '2024-08-14 08:32:10', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1802295445604896769', '1802295407218626562', 't1', 't1', 'directory', '/t1', NULL, '0', '1', 'directory', NULL, 1, '1', '2024-06-16 19:01:34', '1', '2024-08-14 08:32:11', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1802295526521409537', '1802295445604896769', 't2', 't2', 'directory', '/t', NULL, '0', '1', 'directory', NULL, 1, '1', '2024-06-16 19:01:53', '1', '2024-08-14 08:32:11', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1802295905346752514', '1802295526521409537', 'ceshi ', 'ceshi ', 'link', '/t', NULL, '0', '1', 'link', NULL, 1, '1', '2024-06-16 19:03:24', '1', '2024-08-14 08:34:02', '0', NULL, '0', 'https://fanyi.youdao.com/#/', NULL, '0', 'inner');
INSERT INTO `sys_menu` VALUES ('1823518320191033346', '1777538040162844673', '操作日志', '操作日志', 'page', '/operate', '/system/log/SystemOperateLog.vue', '0', '0', 'page', 'FileSyncOutlined', 2, '1', '2024-08-14 08:33:42', '1', '2024-08-14 15:44:24', '0', NULL, '0', NULL, NULL, '0', 'inner');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `priority` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优先级',
  `user_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户范围',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文章内容',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `release_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES ('1', '如何使用Vditor', '1', '1', '3', '0', 'Vditor 是一款**所见即所得**编辑器，支持 *Markdown*。\n\n* 不熟悉 Markdown 可使用工具栏或快捷键进行排版\n* 熟悉 Markdown 可直接排版，也可切换为分屏预览\n\n更多细节和用法请参考 [Vditor - 浏览器端的 Markdown 编辑器](https://ld246.com/article/1549638745630)，同时也欢迎向我们提出建议或报告问题，谢谢 ❤️\n\n## 教程\n\n这是一篇讲解如何正确使用 **Markdown** 的排版示例，学会这个很有必要，能让你的文章有更佳清晰的排版。\n\n> 引用文本：Markdown is a text formatting syntax inspired\n\n## 语法指导\n\n### 普通内容\n\n这段内容展示了在内容里面一些排版格式，比如：\n\n- **加粗** - `**加粗**`\n- *倾斜* - `*倾斜*`\n- ~~删除线~~ - `~~删除线~~`\n- `Code 标记` - `` `Code 标记` ``\n- [超级链接](https://ld246.com) - `[超级链接](https://ld246.com)`\n- [username@gmail.com](mailto:username@gmail.com) - `[username@gmail.com](mailto:username@gmail.com)`\n\n### 提及用户\n\n@Vanessa 通过 `@User` 可以在内容中提及用户，被提及的用户将会收到系统通知。\n\n> NOTE:\n>\n> 1. @用户名之后需要有一个空格\n> 2. 新手没有艾特的功能权限\n\n### 表情符号 Emoji\n\n支持大部分标准的表情符号，可使用输入法直接输入，也可手动输入字符格式。通过输入 `:` 触发自动完成，可在个人设置中[设置常用表情](https://ld246.com/settings/function)。\n\n#### 一些表情例子\n\n😄 😆 😵 😭 😰 😅  😢 😤 😍 😌\n👍 👎 💯 👏 🔔 🎁 ❓ 💣 ❤️ ☕️ 🌀 🙇 💋 🙏 💢\n\n### 大标题 - Heading 3\n\n你可以选择使用 H1 至 H6，使用 ##(N) 打头。建议帖子或回帖中的顶级标题使用 Heading 3，不要使用 1 或 2，因为 1 是系统站点级，2 是帖子标题级。\n\n> NOTE: 别忘了 # 后面需要有空格！\n\n#### Heading 4\n\n##### Heading 5\n\n###### Heading 6\n\n### 图片\n\n```\n![alt 文本](http://image-path.png)\n![alt 文本](http://image-path.png \"图片 Title 值\")\n```\n\n支持复制粘贴直接上传。\n\n### 代码块\n\n#### 普通\n\n```\n*emphasize*    **strong**\n_emphasize_    __strong__\nvar a = 1\n```\n\n#### 语法高亮支持\n\n如果在 ``` 后面跟随语言名称，可以有语法高亮的效果哦，比如:\n\n##### 演示 Go 代码高亮\n\n```go\npackage main\n\nimport \"fmt\"\n\nfunc main() {\n	fmt.Println(\"Hello, 世界\")\n}\n```\n\n##### 演示 Java 高亮\n\n```java\npublic class HelloWorld {\n\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n\n}\n```\n\n> Tip: 语言名称支持下面这些: `ruby`, `python`, `js`, `html`, `erb`, `css`, `coffee`, `bash`, `json`, `yml`, `xml` ...\n\n### 有序、无序、任务列表\n\n#### 无序列表\n\n- Java\n  - Spring\n    - IoC\n    - AOP\n- Go\n  - gofmt\n  - Wide\n- Node.js\n  - Koa\n  - Express\n\n#### 有序列表\n\n1. Node.js\n   1. Express\n   2. Koa\n   3. Sails\n2. Go\n   1. gofmt\n   2. Wide\n3. Java\n   1. Latke\n   2. IDEA\n\n#### 任务列表\n\n- [X] 发布 Sym\n- [X] 发布 Solo\n- [ ] 预约牙医\n\n### 表格\n\n如果需要展示数据什么的，可以选择使用表格。\n\n| header 1 | header 2 |\n| -------- | -------- |\n| cell 1   | cell 2   |\n| cell 3   | cell 4   |\n| cell 5   | cell 6   |\n\n### 隐藏细节\n\n<details>\n<summary>这里是摘要部分。</summary>\n这里是细节部分。\n</details>\n\n### 段落\n\n空行可以将内容进行分段，便于阅读。（这是第一段）\n\n使用空行在 Markdown 排版中相当重要。（这是第二段）\n\n### 链接引用\n\n[链接文本][链接标识]\n\n[链接标识]: https://b3log.org\n```\n[链接文本][链接标识]\n\n[链接标识]: https://b3log.org\n```\n\n### 数学公式\n\n多行公式块：\n\n$$\n\\frac{1}{\n  \\Bigl(\\sqrt{\\phi \\sqrt{5}}-\\phi\\Bigr) e^{\n  \\frac25 \\pi}} = 1+\\frac{e^{-2\\pi}} {1+\\frac{e^{-4\\pi}} {\n    1+\\frac{e^{-6\\pi}}\n    {1+\\frac{e^{-8\\pi}}{1+\\cdots}}\n  }\n}\n$$\n\n行内公式：\n\n公式 $a^2 + b^2 = \\color{red}c^2$ 是行内。\n\n### 脑图\n\n```mindmap\n- 教程\n- 语法指导\n  - 普通内容\n  - 提及用户\n  - 表情符号 Emoji\n    - 一些表情例子\n  - 大标题 - Heading 3\n    - Heading 4\n      - Heading 5\n        - Heading 6\n  - 图片\n  - 代码块\n    - 普通\n    - 语法高亮支持\n      - 演示 Go 代码高亮\n      - 演示 Java 高亮\n  - 有序、无序、任务列表\n    - 无序列表\n    - 有序列表\n    - 任务列表\n  - 表格\n  - 隐藏细节\n  - 段落\n  - 链接引用\n  - 数学公式\n  - 脑图\n  - 流程图\n  - 时序图\n  - 甘特图\n  - 图表\n  - 五线谱\n  - Graphviz\n  - 多媒体\n  - 脚注\n- 快捷键\n```\n\n### 流程图\n\n```mermaid\ngraph TB\n    c1-->a2\n    subgraph one\n    a1-->a2\n    end\n    subgraph two\n    b1-->b2\n    end\n    subgraph three\n    c1-->c2\n    end\n```\n\n### 时序图\n\n```mermaid\nsequenceDiagram\n    Alice->>John: Hello John, how are you?\n    loop Every minute\n        John-->>Alice: Great!\n    end\n```\n\n### 甘特图\n\n```mermaid\ngantt\n    title A Gantt Diagram\n    dateFormat  YYYY-MM-DD\n    section Section\n    A task           :a1, 2019-01-01, 30d\n    Another task     :after a1  , 20d\n    section Another\n    Task in sec      :2019-01-12  , 12d\n    another task      : 24d\n```\n\n### 图表\n\n```echarts\n{\n  \"title\": { \"text\": \"最近 30 天\" },\n  \"tooltip\": { \"trigger\": \"axis\", \"axisPointer\": { \"lineStyle\": { \"width\": 0 } } },\n  \"legend\": { \"data\": [\"帖子\", \"用户\", \"回帖\"] },\n  \"xAxis\": [{\n      \"type\": \"category\",\n      \"boundaryGap\": false,\n      \"data\": [\"2019-05-08\",\"2019-05-09\",\"2019-05-10\",\"2019-05-11\",\"2019-05-12\",\"2019-05-13\",\"2019-05-14\",\"2019-05-15\",\"2019-05-16\",\"2019-05-17\",\"2019-05-18\",\"2019-05-19\",\"2019-05-20\",\"2019-05-21\",\"2019-05-22\",\"2019-05-23\",\"2019-05-24\",\"2019-05-25\",\"2019-05-26\",\"2019-05-27\",\"2019-05-28\",\"2019-05-29\",\"2019-05-30\",\"2019-05-31\",\"2019-06-01\",\"2019-06-02\",\"2019-06-03\",\"2019-06-04\",\"2019-06-05\",\"2019-06-06\",\"2019-06-07\"],\n      \"axisTick\": { \"show\": false },\n      \"axisLine\": { \"show\": false }\n  }],\n  \"yAxis\": [{ \"type\": \"value\", \"axisTick\": { \"show\": false }, \"axisLine\": { \"show\": false }, \"splitLine\": { \"lineStyle\": { \"color\": \"rgba(0, 0, 0, .38)\", \"type\": \"dashed\" } } }],\n  \"series\": [\n    {\n      \"name\": \"帖子\", \"type\": \"line\", \"smooth\": true, \"itemStyle\": { \"color\": \"#d23f31\" }, \"areaStyle\": { \"normal\": {} }, \"z\": 3,\n      \"data\": [\"18\",\"14\",\"22\",\"9\",\"7\",\"18\",\"10\",\"12\",\"13\",\"16\",\"6\",\"9\",\"15\",\"15\",\"12\",\"15\",\"8\",\"14\",\"9\",\"10\",\"29\",\"22\",\"14\",\"22\",\"9\",\"10\",\"15\",\"9\",\"9\",\"15\",\"0\"]\n    },\n    {\n      \"name\": \"用户\", \"type\": \"line\", \"smooth\": true, \"itemStyle\": { \"color\": \"#f1e05a\" }, \"areaStyle\": { \"normal\": {} }, \"z\": 2,\n      \"data\": [\"31\",\"33\",\"30\",\"23\",\"16\",\"29\",\"23\",\"37\",\"41\",\"29\",\"16\",\"13\",\"39\",\"23\",\"38\",\"136\",\"89\",\"35\",\"22\",\"50\",\"57\",\"47\",\"36\",\"59\",\"14\",\"23\",\"46\",\"44\",\"51\",\"43\",\"0\"]\n    },\n    {\n      \"name\": \"回帖\", \"type\": \"line\", \"smooth\": true, \"itemStyle\": { \"color\": \"#4285f4\" }, \"areaStyle\": { \"normal\": {} }, \"z\": 1,\n      \"data\": [\"35\",\"42\",\"73\",\"15\",\"43\",\"58\",\"55\",\"35\",\"46\",\"87\",\"36\",\"15\",\"44\",\"76\",\"130\",\"73\",\"50\",\"20\",\"21\",\"54\",\"48\",\"73\",\"60\",\"89\",\"26\",\"27\",\"70\",\"63\",\"55\",\"37\",\"0\"]\n    }\n  ]\n}\n```\n\n### 五线谱\n\n```abc\nX: 24\nT: Clouds Thicken\nC: Paul Rosen\nS: Copyright 2005, Paul Rosen\nM: 6/8\nL: 1/8\nQ: 3/8=116\nR: Creepy Jig\nK: Em\n|:\"Em\"EEE E2G|\"C7\"_B2A G2F|\"Em\"EEE E2G|\\\n\"C7\"_B2A \"B7\"=B3|\"Em\"EEE E2G|\n\"C7\"_B2A G2F|\"Em\"GFE \"D (Bm7)\"F2D|\\\n1\"Em\"E3-E3:|2\"Em\"E3-E2B|:\"Em\"e2e gfe|\n\"G\"g2ab3|\"Em\"gfeg2e|\"D\"fedB2A|\"Em\"e2e gfe|\\\n\"G\"g2ab3|\"Em\"gfe\"D\"f2d|\"Em\"e3-e3:|\n```\n\n### Graphviz\n\n```graphviz\ndigraph finite_state_machine {\n    rankdir=LR;\n    size=\"8,5\"\n    node [shape = doublecircle]; S;\n    node [shape = point ]; qi\n\n    node [shape = circle];\n    qi -> S;\n    S  -> q1 [ label = \"a\" ];\n    S  -> S  [ label = \"a\" ];\n    q1 -> S  [ label = \"a\" ];\n    q1 -> q2 [ label = \"ddb\" ];\n    q2 -> q1 [ label = \"b\" ];\n    q2 -> q2 [ label = \"b\" ];\n}\n```\n\n### Flowchart\n\n```flowchart\nst=>start: Start\nop=>operation: Your Operation\ncond=>condition: Yes or No?\ne=>end\n\nst->op->cond\ncond(yes)->e\ncond(no)->op\n```\n\n### 多媒体\n\n支持 v.qq.com，youtube.com，youku.com，coub.com，facebook.com/video，dailymotion.com，.mp4，.m4v，.ogg，.ogv，.webm，.mp3，.wav 链接解析\n\nhttps://v.qq.com/x/cover/zf2z0xpqcculhcz/y0016tj0qvh.html\n\n### 脚注\n\n这里是一个脚注引用[^1]，这里是另一个脚注引用[^bignote]。\n\n[^1]: 第一个脚注定义。\n    \n[^bignote]: 脚注定义可使用多段内容。\n    \n       缩进对齐的段落包含在这个脚注定义内。\n    \n       ```\n       可以使用代码块。\n       ```\n       还有其他行级排版语法，比如**加粗**和[链接](https://b3log.org)。\n    \n```\n这里是一个脚注引用[^1]，这里是另一个脚注引用[^bignote]。\n[^1]: 第一个脚注定义。\n[^bignote]: 脚注定义可使用多段内容。\n\n    缩进对齐的段落包含在这个脚注定义内。\n\n    ```\n    可以使用代码块。\n    ```\n\n    还有其他行级排版语法，比如**加粗**和[链接](https://b3log.org)。\n```\n\n## 快捷键\n\n我们的编辑器支持很多快捷键，具体请参考 [键盘快捷键](https://ld246.com/article/1474030007391)（或者按 \"`?` \"😼）\n', '0', '1', '2024-07-02 13:47:20', '1', '2024-07-29 22:21:47', '2024-07-29 22:21:47', 'ceshi');

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务描述',
  `type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型编码',
  `type_msg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型描述',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名',
  `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名',
  `ip_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id（操作人）',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_operate_log
-- ----------------------------
INSERT INTO `sys_operate_log` VALUES ('1824084940907266050', '清空操作日志', 'DELETE', '删除数据', 'com.lihua.system.controller.SysLogController', 'clearOperateLog', '0:0:0:0:0:0:0:1', '1', 'Yukino', 'admin', '2024-08-15 22:05:14', 4, NULL, '{\"code\":200,\"msg\":\"成功\",\"data\":null}', NULL, NULL, '/system/log/operate/clear', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_operate_log` VALUES ('1824085014978674690', '清空登录日志', 'DELETE', '删除数据', 'com.lihua.system.controller.SysLogController', 'clearLoginLog', '0:0:0:0:0:0:0:1', '1', 'Yukino', 'admin', '2024-08-15 22:05:32', 4, NULL, '{\"code\":200,\"msg\":\"成功\",\"data\":null}', NULL, NULL, '/system/log/login/clear', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_operate_log` VALUES ('1824992508034912257', '保存用户', 'SAVE', '保存数据', 'com.lihua.system.controller.SysUserController', 'save', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '2024-08-18 10:11:35', 58, '{\"sysUserDTO\":\"{\\\"pageNum\\\":null,\\\"pageSize\\\":null,\\\"id\\\":\\\"1807687880993570818\\\",\\\"deptIdList\\\":[\\\"1810226204790657025\\\",\\\"1810226204790657026\\\"],\\\"roleIdList\\\":[\\\"1779067819464077314\\\"],\\\"postIdList\\\":[\\\"1\\\",\\\"1810589506918060035\\\",\\\"2\\\",\\\"3\\\"],\\\"nickname\\\":\\\"Yukino\\\",\\\"username\\\":\\\"yukino\\\",\\\"password\\\":null,\\\"gender\\\":\\\"1\\\",\\\"phoneNumber\\\":null,\\\"status\\\":\\\"0\\\",\\\"email\\\":null,\\\"remark\\\":null,\\\"defaultDeptId\\\":null,\\\"createTimeList\\\":null}\"}', '{\"code\":200,\"msg\":\"成功\",\"data\":\"1807687880993570818\"}', NULL, NULL, '/system/user', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_operate_log` VALUES ('1824992962710024193', '保存用户', 'SAVE', '保存数据', 'com.lihua.system.controller.SysUserController', 'save', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '2024-08-18 10:13:24', 66, '{\"sysUserDTO\":\"{\\\"pageNum\\\":null,\\\"pageSize\\\":null,\\\"id\\\":\\\"1807687880993570818\\\",\\\"deptIdList\\\":[\\\"1810226204790657025\\\",\\\"1810226204790657026\\\"],\\\"roleIdList\\\":[\\\"1779067819464077314\\\"],\\\"postIdList\\\":[\\\"1\\\",\\\"1810589506918060035\\\",\\\"2\\\",\\\"3\\\"],\\\"nickname\\\":\\\"Yukino\\\",\\\"username\\\":\\\"yukino\\\",\\\"password\\\":null,\\\"gender\\\":\\\"1\\\",\\\"phoneNumber\\\":null,\\\"status\\\":\\\"0\\\",\\\"email\\\":null,\\\"remark\\\":null,\\\"defaultDeptId\\\":null,\\\"createTimeList\\\":null}\"}', '{\"code\":200,\"msg\":\"成功\",\"data\":\"1807687880993570818\"}', NULL, NULL, '/system/user', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_operate_log` VALUES ('1824993204968828930', '保存用户', 'SAVE', '保存数据', 'com.lihua.system.controller.SysUserController', 'save', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '2024-08-18 10:14:21', 31, '{\"sysUserDTO\":\"{\\\"pageNum\\\":null,\\\"pageSize\\\":null,\\\"id\\\":\\\"1807687880993570818\\\",\\\"deptIdList\\\":[\\\"1810226204790657025\\\",\\\"1810226204790657026\\\"],\\\"roleIdList\\\":[\\\"1779067819464077314\\\"],\\\"postIdList\\\":[\\\"1\\\",\\\"1810589506918060035\\\",\\\"2\\\",\\\"3\\\"],\\\"nickname\\\":\\\"Yukino\\\",\\\"username\\\":\\\"yukino\\\",\\\"password\\\":null,\\\"gender\\\":\\\"1\\\",\\\"phoneNumber\\\":null,\\\"status\\\":\\\"0\\\",\\\"email\\\":null,\\\"remark\\\":null,\\\"defaultDeptId\\\":null,\\\"createTimeList\\\":null}\"}', '{\"code\":200,\"msg\":\"成功\",\"data\":\"1807687880993570818\"}', NULL, NULL, '/system/user', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_operate_log` VALUES ('1824993314687627265', '保存用户', 'SAVE', '保存数据', 'com.lihua.system.controller.SysUserController', 'save', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '2024-08-18 10:14:48', 33, '{\"sysUserDTO\":\"{\\\"pageNum\\\":null,\\\"pageSize\\\":null,\\\"id\\\":\\\"1807687880993570818\\\",\\\"deptIdList\\\":[\\\"1810226204790657025\\\",\\\"1810226204790657026\\\"],\\\"roleIdList\\\":[\\\"1779067819464077314\\\"],\\\"postIdList\\\":[\\\"1\\\",\\\"1810589506918060035\\\",\\\"2\\\",\\\"3\\\"],\\\"nickname\\\":\\\"Yukino\\\",\\\"username\\\":\\\"yukino\\\",\\\"password\\\":null,\\\"gender\\\":\\\"1\\\",\\\"phoneNumber\\\":null,\\\"status\\\":\\\"0\\\",\\\"email\\\":null,\\\"remark\\\":null,\\\"defaultDeptId\\\":null,\\\"createTimeList\\\":null}\"}', '{\"code\":200,\"msg\":\"成功\",\"data\":\"1807687880993570818\"}', NULL, NULL, '/system/user', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');
INSERT INTO `sys_operate_log` VALUES ('1824994030751821825', '保存用户', 'SAVE', '保存数据', 'com.lihua.system.controller.SysUserController', 'save', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, '2024-08-18 10:17:38', 58, '{\"sysUserDTO\":\"{\\\"pageNum\\\":null,\\\"pageSize\\\":null,\\\"id\\\":\\\"1807687880993570818\\\",\\\"deptIdList\\\":[\\\"1810226204790657025\\\",\\\"1810226204790657026\\\"],\\\"roleIdList\\\":[\\\"1779067819464077314\\\"],\\\"postIdList\\\":[\\\"1\\\",\\\"1810589506918060035\\\",\\\"2\\\",\\\"3\\\"],\\\"nickname\\\":\\\"Yukino\\\",\\\"username\\\":\\\"yukino\\\",\\\"password\\\":null,\\\"gender\\\":\\\"1\\\",\\\"phoneNumber\\\":null,\\\"status\\\":\\\"0\\\",\\\"email\\\":null,\\\"remark\\\":null,\\\"defaultDeptId\\\":null,\\\"createTimeList\\\":null}\"}', '{\"code\":200,\"msg\":\"成功\",\"data\":\"1807687880993570818\"}', NULL, NULL, '/system/user', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0', '0', '0');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 主键',
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
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统单位/岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES ('1810589506918060034', '1810226204790657025', 'xiaomi', '开发一部', 'kf11', 1, '0', '张三', '15510916240', '2651518588@qq.com', '123456', '0', '1', '2024-07-09 16:19:12', NULL, NULL, '备注');
INSERT INTO `sys_post` VALUES ('1810589506918060035', '1810226204790657025', 'xiaomi', '开发二部', 'kf21', 2, '0', NULL, NULL, NULL, NULL, '0', '1', '2024-07-09 16:19:12', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色编码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'ROLE_admin', '0', '0', NULL, '2024-05-16 21:32:55', '1', '2024-06-19 22:49:48', NULL);
INSERT INTO `sys_role` VALUES ('1779067819464077314', ' 一般角色', 'ROLE_common', '0', '0', '1', '2024-04-13 16:43:16', '1', '2024-06-19 22:49:07', NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  `menu_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '031f293f02c84e4d9e27f866e18bc019');
INSERT INTO `sys_role_menu` VALUES ('1', '031f293f02c84e4d9e27f866e18bc059');
INSERT INTO `sys_role_menu` VALUES ('1', '1775035631645659138');
INSERT INTO `sys_role_menu` VALUES ('1', '1775365169634258945');
INSERT INTO `sys_role_menu` VALUES ('1', '1775365569678585857');
INSERT INTO `sys_role_menu` VALUES ('1', '1776075821136220161');
INSERT INTO `sys_role_menu` VALUES ('1', '1776946902768373762');
INSERT INTO `sys_role_menu` VALUES ('1', '1776947169823903746');
INSERT INTO `sys_role_menu` VALUES ('1', '1776947236303622146');
INSERT INTO `sys_role_menu` VALUES ('1', '1776948046425051138');
INSERT INTO `sys_role_menu` VALUES ('1', '1776948212783730690');
INSERT INTO `sys_role_menu` VALUES ('1', '1776948371953373186');
INSERT INTO `sys_role_menu` VALUES ('1', '1776948618620391426');
INSERT INTO `sys_role_menu` VALUES ('1', '1777536058626834433');
INSERT INTO `sys_role_menu` VALUES ('1', '1777536311941824513');
INSERT INTO `sys_role_menu` VALUES ('1', '1777536895235293186');
INSERT INTO `sys_role_menu` VALUES ('1', '1777538040162844673');
INSERT INTO `sys_role_menu` VALUES ('1', '1777538768721838082');
INSERT INTO `sys_role_menu` VALUES ('1', '1777539832263114753');
INSERT INTO `sys_role_menu` VALUES ('1', '1784084466574819330');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '031f293f02c84e4d9e27f866e18bc019');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '031f293f02c84e4d9e27f866e18bc059');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1775035631645659138');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1776946902768373762');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1776947169823903746');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1776947236303622146');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1776948046425051138');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1776948212783730690');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1776948371953373186');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1776948618620391426');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1777536058626834433');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1777536311941824513');
INSERT INTO `sys_role_menu` VALUES ('1778051355835736065', '1777536895235293186');
INSERT INTO `sys_role_menu` VALUES ('1779067819464077314', '031f293f02c84e4d9e27f866e18bc019');
INSERT INTO `sys_role_menu` VALUES ('1779067819464077314', '1776946902768373762');
INSERT INTO `sys_role_menu` VALUES ('1779067819464077314', '1776947169823903746');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户状态',
  `theme` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户系统主题json',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_user_username`(`username` ASC) USING BTREE,
  INDEX `idx_sys_user_email`(`email` ASC) USING BTREE,
  INDEX `idx_sys_user_phone_number`(`phone_number` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$Z4TZ6xukbmrYLsvPC1R7SupnvyRhhRbPGtIuwK4rBx9EzQPA6pXEO', 'Yukino', '{\"value\":\"/Users/shangxuan/项目/lihuaFile/upload/4e00f2cf6aeb447590a68cc5f5a05605.png\",\"type\":\"image\",\"backgroundColor\":\"rgb(245, 34, 45)\"}', '1', '0', '{\"layoutType\":\"sider-header\",\"componentSize\":\"default\",\"showViewTabs\":true,\"isDarkTheme\":false,\"colorPrimary\":\"rgb(22, 119, 255)\",\"siderTheme\":\"light\",\"groundGlass\":true,\"affixHead\":true,\"layoutBackgroundColor\":\"rgba(255,255,255,0.6)\",\"siderBackgroundColor\":\"rgba(255,255,255,1)\",\"siderMode\":\"inline\",\"siderGroup\":true,\"siderWith\":220,\"originSiderWith\":220,\"routeTransition\":\"breathe\",\"themeConfig\":{\"token\":{\"colorPrimary\":\"rgb(22, 119, 255)\"}}}', '0', NULL, '2024-06-02 16:57:25', '1', '2024-08-18 15:18:17', '2651518588@qq.com', '15510916240', NULL);
INSERT INTO `sys_user` VALUES ('1807687880993570818', 'yukino', '$2a$10$Sz.ZAEbiHpsYZ7N9aYMAGu6K14ZLbMMbycywtW2gZpOLy8uf1IyBq', 'Yukino', '{\"value\":\"NintendoSwitch\",\"type\":\"icon\",\"backgroundColor\":\"rgb(245, 34, 45)\"}', '1', '0', '{\"layoutType\":\"sider-header\",\"componentSize\":\"default\",\"showViewTabs\":true,\"isDarkTheme\":false,\"colorPrimary\":\"rgb(22, 119, 255)\",\"siderTheme\":\"light\",\"groundGlass\":true,\"affixHead\":true,\"layoutBackgroundColor\":\"rgba(255,255,255,0.6)\",\"siderBackgroundColor\":\"rgba(255,255,255,1)\",\"siderMode\":\"inline\",\"siderGroup\":false,\"siderWith\":200,\"originSiderWith\":200,\"routeTransition\":\"zoom\",\"themeConfig\":{\"token\":{\"colorPrimary\":\"#2196F3\"}}}', '0', '1', '2024-08-18 10:17:38', '1', '2024-07-19 15:26:53', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `dept_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '绑定时间',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定人id',
  `default_dept` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认单位',
  PRIMARY KEY (`user_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES ('1', '1810226204790657025', '2024-07-08 16:59:47', '1', '0');
INSERT INTO `sys_user_dept` VALUES ('1807687880993570818', '1810226204790657025', '2024-08-18 10:17:38', '1', '1');
INSERT INTO `sys_user_dept` VALUES ('1807687880993570818', '1810226204790657026', '2024-08-18 10:17:38', '1', '1');

-- ----------------------------
-- Table structure for sys_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_notice`;
CREATE TABLE `sys_user_notice`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `notice_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告id',
  `star_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'star标记',
  `read_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '已读标记',
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  PRIMARY KEY (`user_id`, `notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_notice
-- ----------------------------
INSERT INTO `sys_user_notice` VALUES ('1', '0', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1', '1', '0', '1', '2024-07-29 22:21:51');
INSERT INTO `sys_user_notice` VALUES ('1807687880993570818', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889507333083137', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889523195940865', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889548042997762', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889563889078273', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889579252813826', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889598009741313', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889620436684802', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889645401182210', '1', '0', '0', NULL);
INSERT INTO `sys_user_notice` VALUES ('1817889670327930881', '1', '0', '0', NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `post_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '绑定时间',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES ('1', '1', '2024-06-02 16:57:25', '1');
INSERT INTO `sys_user_post` VALUES ('1', '3', '2024-06-02 16:57:25', '1');
INSERT INTO `sys_user_post` VALUES ('1807687880993570818', '1', '2024-08-18 10:17:38', '1');
INSERT INTO `sys_user_post` VALUES ('1807687880993570818', '1810589506918060035', '2024-08-18 10:17:38', '1');
INSERT INTO `sys_user_post` VALUES ('1807687880993570818', '2', '2024-08-18 10:17:38', '1');
INSERT INTO `sys_user_post` VALUES ('1807687880993570818', '3', '2024-08-18 10:17:38', '1');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `role_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '绑定时间',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '2024-06-02 16:57:25', '1');
INSERT INTO `sys_user_role` VALUES ('1797150775353098241', '1', '2024-06-02 14:18:36', '1');
INSERT INTO `sys_user_role` VALUES ('1797159025205149697', '1', '2024-06-02 14:59:36', '1797159025205149697');
INSERT INTO `sys_user_role` VALUES ('1807687880993570818', '1779067819464077314', '2024-08-18 10:17:38', '1');

-- ----------------------------
-- Table structure for sys_user_setting
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_setting`;
CREATE TABLE `sys_user_setting`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `setting_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录后设置id',
  `execute` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下次登陆后执行',
  `execute_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  `is_skip` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否跳过',
  PRIMARY KEY (`user_id`, `setting_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和登陆后设置关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_setting
-- ----------------------------

-- ----------------------------
-- Table structure for sys_view_tab
-- ----------------------------
DROP TABLE IF EXISTS `sys_view_tab`;
CREATE TABLE `sys_view_tab`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `menu_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由路径key',
  `affix` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否固定（1固定，0不固定）',
  `star` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否收藏（1收藏，0不收藏）',
  PRIMARY KEY (`user_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户菜单收藏管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_view_tab
-- ----------------------------
INSERT INTO `sys_view_tab` VALUES ('1', '031f293f02c84e4d9e27f866e18bc019', '0', '1');
INSERT INTO `sys_view_tab` VALUES ('1', '031f293f02c84e4d9e27f866e18bc059', '0', '1');
INSERT INTO `sys_view_tab` VALUES ('1', '1775365569678585857', '0', '0');
INSERT INTO `sys_view_tab` VALUES ('1', '1777536311941824513', '0', '1');
INSERT INTO `sys_view_tab` VALUES ('1', '1777536895235293186', '0', '0');
INSERT INTO `sys_view_tab` VALUES ('1', '1784084466574819330', '0', '0');

SET FOREIGN_KEY_CHECKS = 1;
