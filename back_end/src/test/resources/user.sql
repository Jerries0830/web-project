/*
Navicat MySQL Data Transfer

Source Server         : Localhost
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2017-06-23 14:25:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `register_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'testUser', 'test', '2022-05-02 14:24:23');
INSERT INTO `user` VALUES ('2', '2@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('3', '3@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('4', '4@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('5', '5@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('6', '6@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('7', '7@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('8', '8@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('9', '9@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
INSERT INTO `user` VALUES ('10', '10@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23');
SET FOREIGN_KEY_CHECKS=1;
