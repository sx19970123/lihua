/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80200
 Source Host           : localhost:3306
 Source Schema         : lihua

 Target Server Type    : MySQL
 Target Server Version : 80200
 File Encoding         : 65001

 Date: 11/04/2024 22:26:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID',
  `parent_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父级字典id',
  `dict_type_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型编码',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典标签',
  `value` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典值',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态',
  `tag_style` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签的样式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1', '0', '1', '是', '0', 2, '系统选项是', '0', NULL, NULL, '1', '2024-03-14 13:31:41', '1', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768153329837694977', '0', '1766758645678043137', '树形字典', 'tree', 1, NULL, '0', '1', '2024-03-14 13:52:59', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768153331267952642', '0', '1766758645678043137', '一般字典', 'default', 2, NULL, '0', '1', '2024-03-14 13:53:00', '1', '2024-03-14 13:59:21', '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768169289755815938', '1768153332689821697', '1766758645678043137', '其他2', 'o1', 1, NULL, '0', '1', '2024-03-14 14:56:24', '1', '2024-03-14 15:27:38', '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768178926144090113', '1768169289755815938', '1766758645678043137', 'qita3', 'o3', 1, NULL, '0', '1', '2024-03-14 15:34:42', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768445908919676929', '1768153332689821697', '1766758645678043137', '123', '312', 2, NULL, '0', '1', '2024-03-15 09:15:35', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768446977636724738', '1768153332689821697', '1766758645678043137', '666', '666', 3, NULL, '0', '1', '2024-03-15 09:19:50', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768511746280398850', '1768153331267952642', '1766758645678043137', '1', '1', 1, NULL, '0', '1', '2024-03-15 13:37:12', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768512214658326529', '1768153331267952642', '1766758645678043137', '2', '2', 2, NULL, '0', '1', '2024-03-15 13:39:04', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768512477536329729', '1768153331267952642', '1766758645678043137', '3', '3', 3, NULL, '0', '1', '2024-03-15 13:40:07', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768514109091225601', '1768153331267952642', '1766758645678043137', '4', '4', 4, NULL, '0', '1', '2024-03-15 13:46:36', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768521531268669442', '1768514109091225601', '1766758645678043137', '5', '5', 1, NULL, '0', '1', '2024-03-15 14:16:05', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768521752086192130', '1768153331267952642', '1766758645678043137', '5', '5', 5, NULL, '0', '1', '2024-03-15 14:16:58', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768521971699949570', '1768153331267952642', '1766758645678043137', '6', '6', 6, NULL, '0', '1', '2024-03-15 14:17:50', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768522081397776385', '1768153331267952642', '1766758645678043137', '7', '7', 7, NULL, '0', '1', '2024-03-15 14:18:16', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768522274612584449', '1768153331267952642', '1766758645678043137', '8', '8', 8, NULL, '0', '1', '2024-03-15 14:19:02', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768522468615921666', '1768153331267952642', '1766758645678043137', '9', '9', 90, NULL, '0', '1', '2024-03-15 14:19:49', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768523041901780994', '1768153331267952642', '1766758645678043137', '10', '10', 913, NULL, '0', '1', '2024-03-15 14:22:05', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768523105671979010', '1768153331267952642', '1766758645678043137', '11', '11', 9141, NULL, '0', '1', '2024-03-15 14:22:21', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768523826605727745', '1768153331267952642', '1766758645678043137', '12', '12', 9142, NULL, '0', '1', '2024-03-15 14:25:13', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768524286381137922', '1768153329837694977', '1766758645678043137', '1', '1', 1, NULL, '0', '1', '2024-03-15 14:27:02', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768524400394903553', '1768153329837694977', '1766758645678043137', '2', '2', 2, NULL, '0', '1', '2024-03-15 14:27:29', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768524459643641857', '1768153329837694977', '1766758645678043137', '3', '3', 3, NULL, '0', '1', '2024-03-15 14:27:43', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768524632725790722', '1768153329837694977', '1766758645678043137', '4', '4', 4, NULL, '0', '1', '2024-03-15 14:28:25', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768525031620878338', '1768153329837694977', '1766758645678043137', '5', '5', 55, NULL, '0', '1', '2024-03-15 14:30:00', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768525148650348545', '1768153329837694977', '1766758645678043137', '7', '7', 56, NULL, '0', '1', '2024-03-15 14:30:28', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768525278619246593', '1768153329837694977', '1766758645678043137', '123', '123', 57, NULL, '0', '1', '2024-03-15 14:30:59', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768525360294928385', '1768153329837694977', '1766758645678043137', '13123', '123123', 58, NULL, '0', '1', '2024-03-15 14:31:18', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768525473704714241', '1768153329837694977', '1766758645678043137', '44', '44', 59, NULL, '0', '1', '2024-03-15 14:31:45', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768525621910446081', '1768153329837694977', '1766758645678043137', '1', '1', 602, NULL, '0', '1', '2024-03-15 14:32:21', NULL, NULL, '1', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1768553675202408450', '1768527062662258690', '1766758645678043137', '4', '4', 3, NULL, '0', '1', '2024-03-15 16:23:49', NULL, NULL, '0', NULL);
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1770356149437501442', '0', 'yes-no', '测试', 'test', 1, NULL, '0', '1', '2024-03-20 15:46:12', '1', '2024-03-20 16:00:47', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771163317281083393', '0', 'sys_status', '正常', '0', 1, NULL, '0', '1', '2024-03-22 21:13:36', '1', '2024-03-24 13:42:09', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771163394133315585', '0', 'sys_status', '停用', '1', 2, NULL, '0', '1', '2024-03-22 21:13:54', '1', '2024-03-22 21:18:01', '0', 'error');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771165766595235841', '0', 'sys_dict_type', '一般字典', '0', 1, NULL, '0', '1', '2024-03-22 21:23:20', '1', '2024-03-24 20:46:11', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771165768948240385', '0', 'sys_dict_type', '树型字典', '1', 2, NULL, '0', '1', '2024-03-22 21:23:21', '1', '2024-03-24 20:46:12', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771166154488664065', '0', 'sys_dict_tag_style', '默认', 'default', 1, NULL, '0', '1', '2024-03-22 21:24:53', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771166156803919874', '0', 'sys_dict_tag_style', '主要', 'processing', 2, NULL, '0', '1', '2024-03-22 21:24:53', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771166159454720001', '0', 'sys_dict_tag_style', '成功', 'success', 3, NULL, '0', '1', '2024-03-22 21:24:54', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771166162894049282', '0', 'sys_dict_tag_style', '警告', 'warning', 4, NULL, '0', '1', '2024-03-22 21:24:55', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771166168740909057', '0', 'sys_dict_tag_style', '错误', 'error', 5, NULL, '0', '1', '2024-03-22 21:24:56', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1771882551757778945', '0', 'sys_dict_tag_style', '蓝色', 'blue', 6, NULL, '0', '1', '2024-03-24 20:51:35', '1', '2024-03-25 22:19:22', '0', 'blue');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1772463094826942465', '0', 'sys_dict_tag_style', '红色', 'red', 7, NULL, '0', '1', '2024-03-26 11:18:27', NULL, NULL, '0', 'red');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1772466547863207937', '0', 'sys_language', '中文', 'cn', 1, NULL, '0', '1', '2024-03-26 11:32:11', '1', '2024-03-27 15:04:17', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1772466549687730178', '0', 'sys_language', '英语', 'en', 2, NULL, '0', '1', '2024-03-26 11:32:11', '1', '2024-03-27 15:04:20', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1773701158649315330', '0', 'sys_menu_type', '目录', 'directory', 1, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-03-30 22:40:29', '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1773701160050212865', '0', 'sys_menu_type', '页面', 'page', 2, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-04-05 11:07:54', '0', 'processing');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1773701161455304706', '0', 'sys_menu_type', '权限', 'perms', 4, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-04-07 20:17:27', '0', 'warning');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1774046926568538113', '0', 'sys_menu_type', '链接', 'link', 3, NULL, '0', '1', '2024-03-30 20:12:02', '1', '2024-04-05 11:08:03', '0', 'success');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1774250426447544321', '0', 'sys_link_menu_open_type', '系统内', 'inner', 1, NULL, '0', '1', '2024-03-31 09:40:40', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1774250428016214017', '0', 'sys_link_menu_open_type', '新页面', 'new-page', 2, NULL, '0', '1', '2024-03-31 09:40:41', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1774252801971306497', '0', 'sys_whether', '是', '0', 1, NULL, '0', '1', '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1774252803737108481', '0', 'sys_whether', '否', '1', 2, NULL, '0', '1', '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('1778063479844745217', '0', 'ce', 'ce', 'ce', 1, NULL, '1', '1', '2024-04-10 22:12:23', '1', '2024-04-10 22:12:30', '1', 'default');
INSERT INTO `sys_dict_data` (`id`, `parent_id`, `dict_type_code`, `label`, `value`, `sort`, `remark`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `status`, `tag_style`) VALUES ('2', '0', '1', '否', '1', 1, '系统选项2否', '0', NULL, NULL, '1', '2024-03-15 16:30:56', '0', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型名称',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型编码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典类型',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典备注',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '字典状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1771163166122561537', '系统状态', 'sys_status', '0', '系统通用状态标识', '1', '2024-03-22 21:13:00', '1', '2024-03-31 20:30:36', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1771164641267666946', '字典标签样式', 'sys_dict_tag_style', '0', '字典配置配置中，样式列的字典', '1', '2024-03-22 21:18:52', '1', '2024-03-22 21:20:00', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1771165529122131969', '字典类型', 'sys_dict_type', '0', '区分字典为一般字典还是树型字典', '1', '2024-03-22 21:22:23', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1772466287761833985', '系统语言', 'sys_language', '0', '系统国际化语言配置', '1', '2024-03-26 11:31:08', '1', '2024-03-26 11:32:50', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1773700957867982850', '菜单类型', 'sys_menu_type', '0', '系统菜单配置类型，分为 目录、页面、链接、权限', '1', '2024-03-29 21:17:17', '1', '2024-04-01 09:59:35', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1774249964684034050', '链接菜单打开方式', 'sys_link_menu_open_type', '0', '链接菜单打开方式，分为系统内嵌套和浏览器新页面', '1', '2024-03-31 09:38:50', '1', '2024-03-31 09:41:45', '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1774252683993923586', '系统是否', 'sys_whether', '0', '系统是否选项字典', '1', '2024-03-31 09:49:39', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1778063440363761665', 'ce', 'ce', '0', NULL, '1', '2024-04-10 22:12:14', '1', '2024-04-10 22:12:39', '1', '1');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1778067479683981314', 'ce ', 'ce', '0', NULL, '1', '2024-04-10 22:28:17', '1', '2024-04-10 22:31:43', '1', '1');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1778067499556593666', 'ce', 'cece', '0', NULL, '1', '2024-04-10 22:28:21', '1', '2024-04-10 22:31:46', '1', '1');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1778067521006264322', 'cece', 'cecece', '0', NULL, '1', '2024-04-10 22:28:27', '1', '2024-04-10 22:31:48', '1', '1');
INSERT INTO `sys_dict_type` (`id`, `name`, `code`, `type`, `remark`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `status`) VALUES ('1778067548164382722', 'cecec', 'cececee', '0', NULL, '1', '2024-04-10 22:28:33', '1', '2024-04-10 22:30:35', '1', '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
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
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人id',
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
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('031f293f02c84e4d9e27f866e18bc019', '1775035631645659138', '菜单管理', '菜单管理', 'page', '/menu', '/system/menu/SystemMenu.vue', '0', '0', NULL, 'BarsOutlined', 1, NULL, NULL, '1', '2024-04-07 20:12:50', '0', NULL, '0', '111', NULL, '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('031f293f02c84e4d9e27f866e18bc059', '1775035631645659138', '字典管理', '字典管理', 'page', '/dict', '/system/dict/SystemDict.vue', '0', '0', 'page', 'ReadOutlined', 2, NULL, NULL, '1', '2024-04-10 16:20:45', '0', NULL, '0', '22222', '{\"name\": \"John\"}', '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1775035631645659138', '0', '系统管理', '系统管理', 'directory', '/system', NULL, '0', '0', 'directory', 'SettingOutlined', 2, '1', '2024-04-02 13:40:48', '1', '2024-04-09 11:40:33', '0', NULL, '1', NULL, NULL, '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1775365169634258945', '0', '外部链接', '外部链接', 'directory', '/link', NULL, '0', '0', 'directory', 'NodeIndexOutlined', 4, '1', '2024-04-03 11:30:16', '1', '2024-04-09 11:41:26', '0', NULL, '1', NULL, NULL, '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1775365569678585857', '1775365169634258945', '系统内显示', '系统内显示', 'link', '/inner', NULL, '0', '0', 'link', 'ChromeOutlined', 1, '1', '2024-04-03 11:31:51', '1', '2024-04-10 16:02:00', '0', NULL, '1', 'http://boot3.jeecg.com/dashboard/analysis', NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776075821136220161', '1775365169634258945', '系统外显示', NULL, 'link', '/sys-out', NULL, '0', '0', NULL, 'ChromeOutlined', 2, '1', '2024-04-05 10:34:08', NULL, NULL, '0', NULL, '1', 'https://gitee.com/yukino_git/lihua', NULL, '0', 'new-page');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776946902768373762', '031f293f02c84e4d9e27f866e18bc019', '列表查询', NULL, 'perms', NULL, NULL, '0', '0', 'system:menu:list', NULL, 1, '1', '2024-04-07 20:15:30', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776947169823903746', '031f293f02c84e4d9e27f866e18bc019', '菜单保存', NULL, 'perms', '', NULL, '0', '0', 'system:menu:save', NULL, 2, '1', '2024-04-07 20:16:34', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776947236303622146', '031f293f02c84e4d9e27f866e18bc019', '菜单删除', NULL, 'perms', NULL, NULL, '0', '0', 'system:menu:delete', NULL, 3, '1', '2024-04-07 20:16:50', '1', '2024-04-07 20:19:35', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776948046425051138', '031f293f02c84e4d9e27f866e18bc059', '列表查询', NULL, 'perms', NULL, NULL, '0', '0', 'system:dict:list', NULL, 1, '1', '2024-04-07 20:20:03', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776948212783730690', '031f293f02c84e4d9e27f866e18bc059', '字典保存', NULL, 'perms', '', NULL, '0', '0', 'system:dict:save', NULL, 2, '1', '2024-04-07 20:20:43', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776948371953373186', '031f293f02c84e4d9e27f866e18bc059', '字典删除', NULL, 'perms', '', NULL, '0', '0', 'system:dict:delete', NULL, 3, '1', '2024-04-07 20:21:21', '1', '2024-04-07 20:22:26', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1776948618620391426', '031f293f02c84e4d9e27f866e18bc059', '字典配置', NULL, 'perms', NULL, NULL, '0', '0', 'system:dict:config', NULL, 4, '1', '2024-04-07 20:22:19', NULL, NULL, '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777536058626834433', '1775035631645659138', '角色管理', '角色管理', 'page', '/role', '/system/role/SystemRole.vue', '0', '0', 'page', 'UserSwitchOutlined', 3, '1', '2024-04-09 11:16:36', '1', '2024-04-09 13:39:23', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777536311941824513', '1775035631645659138', '用户管理', '用户管理', 'page', '/user', '/system/user/SystemUser.vue', '0', '0', 'page', 'TeamOutlined', 4, '1', '2024-04-09 11:17:36', '1', '2024-04-09 13:39:30', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777536895235293186', '1775035631645659138', '部门管理', '部门管理', 'page', '/dept', '/system/dept/SystemDept.vue', '0', '0', 'page', 'ApartmentOutlined', 5, '1', '2024-04-09 11:19:56', '1', '2024-04-09 13:39:42', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777537254552928258', '1775035631645659138', '岗位管理', '岗位管理', 'page', '/post', '/system/post/SystemPost.vue', '0', '0', 'page', 'IdcardOutlined', 6, '1', '2024-04-09 11:21:21', '1', '2024-04-09 13:39:50', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777538040162844673', '0', '日志管理', '日志管理', 'directory', '/log', NULL, '0', '0', 'directory', 'FileSearchOutlined', 3, '1', '2024-04-09 11:24:28', '1', '2024-04-09 11:41:34', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777538768721838082', '1777538040162844673', '登录日志', '登录日志', 'page', '/login', '/log/login/LogLogin.vue', '0', '0', 'page', 'FileProtectOutlined', 1, '1', '2024-04-09 11:27:22', '1', '2024-04-09 13:40:15', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777539419447132162', '1777538040162844673', '操作日志', '操作日志', 'page', '/operate', '/log/operate/LogOperate.vue', '0', '0', 'page', 'FileSyncOutlined', 2, '1', '2024-04-09 11:29:57', '1', '2024-04-09 13:40:21', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1777539832263114753', '0', '通知公告', '通知公告', 'page', '/notice', '/Notice.vue', '0', '0', 'page', 'MessageOutlined', 1, '1', '2024-04-09 11:31:36', '1', '2024-04-09 13:40:02', '0', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1778063591245459457', '0', 'ces', 'ces', 'directory', '/ces', NULL, '0', '0', 'directory', NULL, 5, '1', '2024-04-10 22:12:50', NULL, NULL, '1', NULL, '0', NULL, NULL, '0', 'inner');
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `cache`, `link_path`, `query`, `view_tab`, `link_open_type`) VALUES ('1778064366633803777', '0', '测试菜单', '测试菜单', 'directory', '/12321', NULL, '0', '1', 'directory', NULL, 5, '1', '2024-04-10 22:15:54', '1', '2024-04-10 22:19:55', '1', NULL, '0', NULL, NULL, '0', 'inner');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色编码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标识',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最近一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最近一次更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `name`, `code`, `status`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES ('1', '超级管理员', 'ROLE_admin', '0', '0', NULL, NULL, '1', '2024-04-10 22:33:17', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  `menu_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '031f293f02c84e4d9e27f866e18bc019');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '031f293f02c84e4d9e27f866e18bc059');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1775035631645659138');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1775365169634258945');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1775365569678585857');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776075821136220161');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776946902768373762');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776947169823903746');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776947236303622146');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776948046425051138');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776948212783730690');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776948371953373186');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1776948618620391426');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777536058626834433');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777536311941824513');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777536895235293186');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777537254552928258');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777538040162844673');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777538768721838082');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777539419447132162');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '1777539832263114753');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '031f293f02c84e4d9e27f866e18bc019');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '031f293f02c84e4d9e27f866e18bc059');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1775035631645659138');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1776946902768373762');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1776947169823903746');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1776947236303622146');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1776948046425051138');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1776948212783730690');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1776948371953373186');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1776948618620391426');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1777536058626834433');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1777536311941824513');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1777536895235293186');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1778051355835736065', '1777537254552928258');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '性别',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户状态',
  `theme` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户系统主题json',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `login_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后登陆ip',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `avatar`, `gender`, `status`, `theme`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `login_ip`, `login_time`, `email`, `phone_number`) VALUES ('1', 'admin', '$2a$10$Z4TZ6xukbmrYLsvPC1R7SupnvyRhhRbPGtIuwK4rBx9EzQPA6pXEO', 'Yukino', '{\"url\":\"\",\"value\":\"Unfold\",\"type\":\"icon\",\"backgroundColor\":\"rgb(250, 173, 20)\"}', '1', '0', '{\"layoutType\":\"header-sider\",\"showViewTabs\":true,\"dataDark\":false,\"colorPrimary\":\"rgb(22, 119, 255)\",\"siderTheme\":\"light\",\"groundGlass\":true,\"affixHead\":true,\"layoutBackgroundColor\":\"rgba(255,255,255,0.6)\",\"siderBackgroundColor\":\"rgba(255,255,255,1)\",\"siderMode\":\"inline\",\"siderWith\":220,\"originSiderWith\":220,\"routeTransition\":\"breathe\",\"themeConfig\":{\"token\":{\"colorPrimary\":\"rgb(22, 119, 255)\"}}}', '0', NULL, NULL, '1', '2024-04-10 21:46:57', NULL, NULL, '12321311@qq.com', '15523647831');
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `avatar`, `gender`, `status`, `theme`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `login_ip`, `login_time`, `email`, `phone_number`) VALUES ('2', 'test', '123', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept` (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `dept_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门id',
  `create_time` datetime DEFAULT NULL COMMENT '绑定时间',
  `create_id` varchar(60) DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户部门关联表';

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`, `create_time`, `create_id`) VALUES ('1', '1', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `post_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位id',
  `create_time` datetime DEFAULT NULL COMMENT '绑定时间',
  `create_id` varchar(60) DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` (`user_id`, `post_id`, `create_time`, `create_id`) VALUES ('1', '1', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `role_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '绑定时间',
  `create_id` varchar(60) DEFAULT NULL COMMENT '绑定人id',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_time`, `create_id`) VALUES ('1', '1', NULL, NULL);
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_time`, `create_id`) VALUES ('1', '3', NULL, NULL);
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_time`, `create_id`) VALUES ('2', '4', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_view_tab
-- ----------------------------
DROP TABLE IF EXISTS `sys_view_tab`;
CREATE TABLE `sys_view_tab` (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `menu_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由路径key',
  `affix` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否固定（1固定，0不固定）',
  `star` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否收藏（1收藏，0不收藏）',
  PRIMARY KEY (`user_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户菜单收藏管理表';

-- ----------------------------
-- Records of sys_view_tab
-- ----------------------------
BEGIN;
INSERT INTO `sys_view_tab` (`user_id`, `menu_id`, `affix`, `star`) VALUES ('1', '031f293f02c84e4d9e27f866e18bc019', '0', '0');
INSERT INTO `sys_view_tab` (`user_id`, `menu_id`, `affix`, `star`) VALUES ('1', '031f293f02c84e4d9e27f866e18bc059', '0', '0');
INSERT INTO `sys_view_tab` (`user_id`, `menu_id`, `affix`, `star`) VALUES ('1', '1775365569678585857', '0', '0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
