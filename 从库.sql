/*
Navicat MySQL Data Transfer

Source Server         : LD
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : test_s

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-04-02 16:53:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `permission` int(11) DEFAULT NULL COMMENT '权限值1.监控  2.统计',
  `wxc` varchar(30) DEFAULT 'wdl',
  PRIMARY KEY (`id`),
  UNIQUE KEY `XPK管理员表` (`id`),
  UNIQUE KEY `wxc` (`wxc`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='管理员表';

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', '8', null);
INSERT INTO `admin` VALUES ('2', '8', null);
