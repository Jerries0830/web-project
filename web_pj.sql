-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2022-05-02 21:44:03
-- 服务器版本： 8.0.12
-- PHP 版本： 7.3.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `web_pj`
--

-- --------------------------------------------------------

--
-- 表的结构 `scene`
--

CREATE TABLE `scene` (
  `scene_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `scene`
--

INSERT INTO `scene` (`scene_id`, `name`, `path`) VALUES
(1, 'img1', 'test1'),
(2, 'img2', 'test2');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `register_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `register_time`) VALUES
(1, 'testUser', 'test', '2022-05-02 14:24:23'),
(2, '2@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(3, '3@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(4, '4@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(5, '5@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(6, '6@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(7, '7@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(8, '8@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(9, '9@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(10, '10@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '2017-06-23 14:24:23'),
(11, 'test', 'test', '2022-05-02 17:17:34'),
(12, 'test1', 'test', '2022-05-02 17:33:43');

--
-- 转储表的索引
--

--
-- 表的索引 `scene`
--
ALTER TABLE `scene`
  ADD PRIMARY KEY (`scene_id`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `scene`
--
ALTER TABLE `scene`
  MODIFY `scene_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
