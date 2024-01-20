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

 Date: 20/01/2024 22:34:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `parent_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父级菜单id',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单名称',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '鼠标悬浮展示内容',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单类型 group、divider antdesign menu组件用',
  `menu_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单/页面/按钮/外链',
  `router_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由地址',
  `component_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组建路径',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否显示（0显示、1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单状态(0正常、1停用)',
  `disabled` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '禁用',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识符',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单图标',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后一次更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `no_cache` char(1) DEFAULT NULL COMMENT '是否缓存页面（0 缓存、1不缓存）',
  `link_path` varchar(300) DEFAULT NULL COMMENT '外链类型页面地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('05919353547b4f0887a9318ee73b832f', '19dd01c0806e4a0981d76d62f78d5552', '监控页', '监控页', 'page', 'page', 'menu', '/system/menu/index.vue', NULL, '0', NULL, 'admin', NULL, 6, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('0fe0edb2374e4e99a4d3266fe1406557', '6dba376cc8a543109c30f484c240fd30', '基础表单', '基础表单', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 9, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('19dd01c0806e4a0981d76d62f78d5552', '0', '仪表盘', '仪表盘', 'menu', 'menu', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 1, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('6dba376cc8a543109c30f484c240fd30', '0', '表单页', '表单页', 'menu', 'menu', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 2, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('7b22134b42304cec8bb4c825ba75be47', '0', '列表页', '列表页', 'menu', 'menu', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 3, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('80981da36e814adaa5c3c1648c4c1f62', 'dd8a7f5c42c74d3c86b6821c279654eb', '列表', '列表', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 16, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('89428c4bd1d04e4bb149b10a2e576423', '7b22134b42304cec8bb4c825ba75be47', '卡片列表', '卡片列表', 'page', 'page', 'test/index', NULL, NULL, '0', NULL, 'admin', NULL, 12, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('a702173a0cab492987d978497d689df5', '19dd01c0806e4a0981d76d62f78d5552', '欢迎页', '欢迎页', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 7, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('bd71c2f6cc994ce89f665576daf7db77', '19dd01c0806e4a0981d76d62f78d5552', '分析页', '分析页', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 8, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('c87da65bcaff4a418098ec647434eb08', '7b22134b42304cec8bb4c825ba75be47', '查询列表', '查询列表', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 13, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('cc111dfbdfb2442aad2a397a89fbbbeb', '80981da36e814adaa5c3c1648c4c1f62', '表', '表', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 15, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('cfc9411130704e9ea1ae6ce3a5ef8581', '6dba376cc8a543109c30f484c240fd30', '高级表单', '高级表单', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 10, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('dd8a7f5c42c74d3c86b6821c279654eb', '7b22134b42304cec8bb4c825ba75be47', '标准列表', '标准列表', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 14, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('ea37b1ef262e4f86aa1dc5bbd43e62c4', '0', '工作台', '工作台', 'page', 'page', '/index', 'index', NULL, '0', NULL, 'admin', NULL, 4, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('eb8819f1cdca4d11bb2910a806f85d81', '05919353547b4f0887a9318ee73b832f', '控', '控', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 5, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
INSERT INTO `sys_menu` (`id`, `parent_id`, `label`, `title`, `type`, `menu_type`, `router_path`, `component_path`, `visible`, `status`, `disabled`, `perms`, `icon`, `sort`, `create_id`, `create_time`, `update_id`, `update_time`, `del_flag`, `remark`, `no_cache`, `link_path`) VALUES ('f06e86400f074c1496be42fb22113c24', '6dba376cc8a543109c30f484c240fd30', '基础表单', '基础表单', 'page', 'page', NULL, NULL, NULL, '0', NULL, 'admin', NULL, 11, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL);
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
INSERT INTO `sys_role` (`id`, `name`, `code`, `status`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `remark`) VALUES ('1', '管理员', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
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
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '05919353547b4f0887a9318ee73b832f');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '0fe0edb2374e4e99a4d3266fe1406557');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '19dd01c0806e4a0981d76d62f78d5552');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '6dba376cc8a543109c30f484c240fd30');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '7b22134b42304cec8bb4c825ba75be47');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '80981da36e814adaa5c3c1648c4c1f62');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', '89428c4bd1d04e4bb149b10a2e576423');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'a702173a0cab492987d978497d689df5');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'bd71c2f6cc994ce89f665576daf7db77');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'c87da65bcaff4a418098ec647434eb08');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'cc111dfbdfb2442aad2a397a89fbbbeb');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'cfc9411130704e9ea1ae6ce3a5ef8581');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'dd8a7f5c42c74d3c86b6821c279654eb');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'ea37b1ef262e4f86aa1dc5bbd43e62c4');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'eb8819f1cdca4d11bb2910a806f85d81');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES ('1', 'f06e86400f074c1496be42fb22113c24');
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
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑删除标志',
  `create_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `login_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后登陆ip',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `avatar`, `gender`, `status`, `del_flag`, `create_id`, `create_time`, `update_id`, `update_time`, `login_ip`, `login_time`) VALUES ('1', 'admin', '$2a$10$Lz5DW9qCi3APq11hT6/8Hez6mp0lGiRXn20uWRPos3GUNn6lN8dW6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `role_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES ('1', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
