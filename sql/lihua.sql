/*
 Navicat Premium Data Transfer

 Source Server         : mysql80
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : lihua

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 01/04/2024 17:03:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
INSERT INTO `sys_dict_data` VALUES ('1771166154488664065', '0', 'sys_dict_tag_style', '默认', 'default', 1, NULL, '0', '1', '2024-03-22 21:24:53', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1771166156803919874', '0', 'sys_dict_tag_style', '主要', 'processing', 2, NULL, '0', '1', '2024-03-22 21:24:53', NULL, NULL, '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1771166159454720001', '0', 'sys_dict_tag_style', '成功', 'success', 3, NULL, '0', '1', '2024-03-22 21:24:54', NULL, NULL, '0', 'success');
INSERT INTO `sys_dict_data` VALUES ('1771166162894049282', '0', 'sys_dict_tag_style', '警告', 'warning', 4, NULL, '0', '1', '2024-03-22 21:24:55', NULL, NULL, '0', 'warning');
INSERT INTO `sys_dict_data` VALUES ('1771166168740909057', '0', 'sys_dict_tag_style', '错误', 'error', 5, NULL, '0', '1', '2024-03-22 21:24:56', NULL, NULL, '0', 'error');
INSERT INTO `sys_dict_data` VALUES ('1771882551757778945', '0', 'sys_dict_tag_style', '蓝色', 'blue', 6, NULL, '0', '1', '2024-03-24 20:51:35', '1', '2024-03-25 22:19:22', '0', 'blue');
INSERT INTO `sys_dict_data` VALUES ('1772463094826942465', '0', 'sys_dict_tag_style', '红色', 'red', 7, NULL, '0', '1', '2024-03-26 11:18:27', NULL, NULL, '0', 'red');
INSERT INTO `sys_dict_data` VALUES ('1772466547863207937', '0', 'sys_language', '中文', 'cn', 1, NULL, '0', '1', '2024-03-26 11:32:11', '1', '2024-03-27 15:04:17', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1772466549687730178', '0', 'sys_language', '英语', 'en', 2, NULL, '0', '1', '2024-03-26 11:32:11', '1', '2024-03-27 15:04:20', '0', 'processing');
INSERT INTO `sys_dict_data` VALUES ('1773701158649315330', '0', 'sys_menu_type', '目录', 'directory', 1, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-03-30 22:40:29', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1773701160050212865', '0', 'sys_menu_type', '页面', 'page', 2, NULL, '0', '1', '2024-03-29 21:18:05', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1773701161455304706', '0', 'sys_menu_type', '权限', 'perms', 4, NULL, '0', '1', '2024-03-29 21:18:05', '1', '2024-03-31 20:54:58', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774046926568538113', '0', 'sys_menu_type', '链接', 'link', 3, NULL, '0', '1', '2024-03-30 20:12:02', '1', '2024-03-30 20:12:07', '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774250426447544321', '0', 'sys_link_menu_open_type', '系统内', 'inner', 1, NULL, '0', '1', '2024-03-31 09:40:40', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774250428016214017', '0', 'sys_link_menu_open_type', '新页面', 'new-page', 2, NULL, '0', '1', '2024-03-31 09:40:41', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774252801971306497', '0', 'sys_whether', '是', '0', 1, NULL, '0', '1', '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
INSERT INTO `sys_dict_data` VALUES ('1774252803737108481', '0', 'sys_whether', '否', '1', 2, NULL, '0', '1', '2024-03-31 09:50:07', NULL, NULL, '0', 'default');
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
INSERT INTO `sys_dict_type` VALUES ('1771163166122561537', '系统状态', 'sys_status', '0', '系统通用状态标识', '1', '2024-03-22 21:13:00', '1', '2024-03-31 20:30:36', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1771164641267666946', '字典标签样式', 'sys_dict_tag_style', '0', '字典配置配置中，样式列的字典', '1', '2024-03-22 21:18:52', '1', '2024-03-22 21:20:00', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1771165529122131969', '字典类型', 'sys_dict_type', '0', '区分字典为一般字典还是树型字典', '1', '2024-03-22 21:22:23', NULL, NULL, '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1772466287761833985', '系统语言', 'sys_language', '0', '系统国际化语言配置', '1', '2024-03-26 11:31:08', '1', '2024-03-26 11:32:50', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1773700957867982850', '菜单类型', 'sys_menu_type', '0', '系统菜单配置类型，分为 目录、页面、链接、权限', '1', '2024-03-29 21:17:17', '1', '2024-04-01 09:59:35', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1774249964684034050', '链接菜单打开方式', 'sys_link_menu_open_type', '0', '链接菜单打开方式，分为系统内嵌套和浏览器新页面', '1', '2024-03-31 09:38:50', '1', '2024-03-31 09:41:45', '0', '0');
INSERT INTO `sys_dict_type` VALUES ('1774252683993923586', '系统是否', 'sys_whether', '0', '系统是否选项字典', '1', '2024-03-31 09:49:39', NULL, NULL, '0', '0');

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
  `component_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名称，用于页面缓存使用',
  `router_path_key` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'page类型数据从父级到子节点的路由地址拼接',
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
  `no_cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否缓存页面（0 缓存、1不缓存）',
  `link_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外链类型页面地址',
  `query` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由携带的参数',
  `skip` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否跳过view-tabs缓存（0跳过、1不跳过）',
  `link_open_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接打开方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('031f293f02c84e4d9e27f866e18bc019', '0', '菜单管理', '菜单管理', 'page', '/menu', 'system/menu/index.vue', 'index', '/menu', NULL, '0', 'admin', 'StepBackwardOutlined', 1, NULL, NULL, NULL, NULL, '0', NULL, '1', NULL, '0', '1', NULL);
INSERT INTO `sys_menu` VALUES ('031f293f02c84e4d9e27f866e18bc059', '0', '字典管理', '字典管理', 'page', '/dict', 'system/dict/index.vue', 'index', '/dict', NULL, '0', 'admin', 'StepBackwardOutlined', 1, NULL, NULL, NULL, NULL, '0', NULL, '1', NULL, '0', '1', NULL);
INSERT INTO `sys_menu` VALUES ('05919353547b4f0887a9318ee73b832f', '19dd01c0806e4a0981d76d62f78d5552', '监控页', '监控页', 'menu', 'jky', '/system/menu/index.vue', 'index', '/ybp/jky', NULL, '0', 'admin', 'StepBackwardOutlined', 6, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('0fe0edb2374e4e99a4d3266fe1406557', '6dba376cc8a543109c30f484c240fd30', '基础表单', '基础表单', 'page', 'jcbd', 'jcbd/index.vue', 'index', '/bdy/jcbd', NULL, '0', 'admin', 'StepBackwardOutlined', 9, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('1774688275913940994', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, '1', '2024-04-01 14:40:32', NULL, NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('19dd01c0806e4a0981d76d62f78d5552', '0', '仪表盘', '仪表盘', 'menu', '/ybp', NULL, NULL, '/ybp', NULL, '0', 'admin', 'StepBackwardOutlined', 1, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('458c960671c34ef5a143d200b4f171ce', '0', '没子集的空目录', '没子集的空目录', 'menu', 'kml', NULL, NULL, '/kml', NULL, '0', 'admin', 'StepBackwardOutlined', 1, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('6dba376cc8a543109c30f484c240fd30', '0', '表单页', '表单页', 'menu', '/bdy', NULL, NULL, '/bdy', NULL, '0', 'admin', 'StepBackwardOutlined', 2, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('7b22134b42304cec8bb4c825ba75be47', '0', '列表页', '列表页', 'menu', '/lby', NULL, NULL, '/lby', NULL, '0', 'admin', 'StepBackwardOutlined', 3, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('80981da36e814adaa5c3c1648c4c1f62', 'dd8a7f5c42c74d3c86b6821c279654eb', '列表', '列表', 'menu', 'lb', NULL, NULL, '/lby/bzlb/lb', NULL, '0', 'admin', 'StepBackwardOutlined', 16, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('89428c4bd1d04e4bb149b10a2e576423', '7b22134b42304cec8bb4c825ba75be47', '卡片列表', '卡片列表', 'page', 'kplb', 'kplb/index.vue', 'index', '/lby/kplb', NULL, '0', 'admin', 'StepBackwardOutlined', 12, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('a702173a0cab492987d978497d689df5', '19dd01c0806e4a0981d76d62f78d5552', '欢迎页', '欢迎页', 'page', 'hyy', 'hyy/index.vue', 'index', '/ybp/hyy', NULL, '0', 'admin', 'StepBackwardOutlined', 7, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('bd71c2f6cc994ce89f665576daf7db77', '19dd01c0806e4a0981d76d62f78d5552', '分析页', '分析页', 'page', 'fxy', 'fxy/index.vue', 'index', '/ybp/fxy', NULL, '0', 'admin', 'StepBackwardOutlined', 8, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('c87da65bcaff4a418098ec647434eb08', '7b22134b42304cec8bb4c825ba75be47', '查询列表', '查询列表', 'page', 'cxlb', 'cxlb/index.vue', 'index', '/lby/cxlb', NULL, '0', 'admin', 'StepBackwardOutlined', 13, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('cc111dfbdfb2442aad2a397a89fbbbeb', '80981da36e814adaa5c3c1648c4c1f62', '表', '表', 'page', 'b', 'b/index.vue', 'index', '/lby/bzlb/lb/b', NULL, '0', 'admin', 'StepBackwardOutlined', 15, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('cfc9411130704e9ea1ae6ce3a5ef8581', '6dba376cc8a543109c30f484c240fd30', '高级表单', '高级表单', 'page', 'gjbd', 'gjbd/index.vue', 'index', '/bdy/gjbd', NULL, '0', 'admin', 'StepBackwardOutlined', 10, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('dd8a7f5c42c74d3c86b6821c279654eb', '7b22134b42304cec8bb4c825ba75be47', '标准列表', '标准列表', 'menu', 'bzlb', NULL, NULL, '/lby/bzlb', NULL, '0', 'admin', 'StepBackwardOutlined', 14, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('ea37b1ef262e4f86aa1dc5bbd43e62c4', '0', '工作台', '工作台', 'page', 'gzt', 'gzt/index.vue', 'index', '/gzt', NULL, '0', 'admin', 'StepBackwardOutlined', 4, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);
INSERT INTO `sys_menu` VALUES ('eb8819f1cdca4d11bb2910a806f85d81', '05919353547b4f0887a9318ee73b832f', '控', '控', 'page', 'k', 'k/index.vue', 'index', '/ybp/jky/k', NULL, '0', 'admin', 'StepBackwardOutlined', 5, NULL, NULL, NULL, NULL, '1', NULL, '1', NULL, '1', '1', NULL);

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
INSERT INTO `sys_role` VALUES ('1', '管理员', 'admin', '0', '0', NULL, NULL, NULL, NULL, NULL);

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
INSERT INTO `sys_role_menu` VALUES ('1', '05919353547b4f0887a9318ee73b832f');
INSERT INTO `sys_role_menu` VALUES ('1', '0fe0edb2374e4e99a4d3266fe1406557');
INSERT INTO `sys_role_menu` VALUES ('1', '19dd01c0806e4a0981d76d62f78d5552');
INSERT INTO `sys_role_menu` VALUES ('1', '458c960671c34ef5a143d200b4f171ce');
INSERT INTO `sys_role_menu` VALUES ('1', '6dba376cc8a543109c30f484c240fd30');
INSERT INTO `sys_role_menu` VALUES ('1', '7b22134b42304cec8bb4c825ba75be47');
INSERT INTO `sys_role_menu` VALUES ('1', '80981da36e814adaa5c3c1648c4c1f62');
INSERT INTO `sys_role_menu` VALUES ('1', '89428c4bd1d04e4bb149b10a2e576423');
INSERT INTO `sys_role_menu` VALUES ('1', 'a702173a0cab492987d978497d689df5');
INSERT INTO `sys_role_menu` VALUES ('1', 'bd71c2f6cc994ce89f665576daf7db77');
INSERT INTO `sys_role_menu` VALUES ('1', 'c87da65bcaff4a418098ec647434eb08');
INSERT INTO `sys_role_menu` VALUES ('1', 'cc111dfbdfb2442aad2a397a89fbbbeb');
INSERT INTO `sys_role_menu` VALUES ('1', 'cfc9411130704e9ea1ae6ce3a5ef8581');
INSERT INTO `sys_role_menu` VALUES ('1', 'dd8a7f5c42c74d3c86b6821c279654eb');
INSERT INTO `sys_role_menu` VALUES ('1', 'ea37b1ef262e4f86aa1dc5bbd43e62c4');
INSERT INTO `sys_role_menu` VALUES ('1', 'eb8819f1cdca4d11bb2910a806f85d81');
INSERT INTO `sys_role_menu` VALUES ('1', 'f06e86400f074c1496be42fb22113c24');

-- ----------------------------
-- Table structure for sys_star_view
-- ----------------------------
DROP TABLE IF EXISTS `sys_star_view`;
CREATE TABLE `sys_star_view`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `router_path_key` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由路径key',
  `affix` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否固定（1固定，0不固定）',
  `star` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否收藏（1收藏，0不收藏）',
  PRIMARY KEY (`user_id`, `router_path_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户菜单收藏管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_star_view
-- ----------------------------
INSERT INTO `sys_star_view` VALUES ('1', '/bdy/gjbd', '0', '0');
INSERT INTO `sys_star_view` VALUES ('1', '/bdy/jcbd', '0', '0');
INSERT INTO `sys_star_view` VALUES ('1', '/dict', '0', '1');
INSERT INTO `sys_star_view` VALUES ('1', '/gzt', '0', '1');
INSERT INTO `sys_star_view` VALUES ('1', '/gzt/gztzym', '0', '0');
INSERT INTO `sys_star_view` VALUES ('1', '/lby/bzlb/lb/b', '0', '1');
INSERT INTO `sys_star_view` VALUES ('1', '/lby/cxlb', '0', '1');
INSERT INTO `sys_star_view` VALUES ('1', '/lby/kplb', '0', '0');
INSERT INTO `sys_star_view` VALUES ('1', '/menu', '0', '0');
INSERT INTO `sys_star_view` VALUES ('1', '/ybp/fxy', '0', '0');
INSERT INTO `sys_star_view` VALUES ('1', '/ybp/hyy', '0', '1');
INSERT INTO `sys_star_view` VALUES ('1', '/ybp/jky/k', '0', '0');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户状态',
  `theme` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户系统主题json',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `login_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登陆ip',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$Z4TZ6xukbmrYLsvPC1R7SupnvyRhhRbPGtIuwK4rBx9EzQPA6pXEO', 'Yukino', '{\"url\":\"\",\"value\":\"Unfold\",\"type\":\"icon\",\"backgroundColor\":\"rgb(250, 173, 20)\"}', '1', '0', '{\"layoutType\":\"header-sider\",\"showViewTags\":true,\"dataDark\":false,\"colorPrimary\":\"rgb(47, 84, 235)\",\"siderTheme\":\"light\",\"groundGlass\":true,\"affixHead\":true,\"layoutBackgroundColor\":\"rgba(255,255,255,0.6)\",\"siderBackgroundColor\":\"rgba(255,255,255,1)\",\"siderMode\":\"inline\",\"siderWith\":220,\"originSiderWith\":220,\"routeTransition\":\"breathe\",\"themeConfig\":{\"token\":{\"colorPrimary\":\"rgb(47, 84, 235)\"}}}', '0', NULL, NULL, '1', '2024-03-31 11:13:05', NULL, NULL, '12321311@qq.com', '15523647831');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `role_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');

SET FOREIGN_KEY_CHECKS = 1;
