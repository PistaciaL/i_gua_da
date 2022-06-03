DROP TABLE IF EXISTS `user_info`;
DROP TABLE IF EXISTS `bus_schedule`;
DROP TABLE IF EXISTS `station_info`;
DROP TABLE IF EXISTS `reserve_info`;
DROP TABLE IF EXISTS `notice_info`;
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `user_info` (
                             `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户的id',
                             `name` varchar(20) NOT NULL COMMENT '登录的用户名',
                             `password` varchar(20) NOT NULL DEFAULT '' COMMENT '密码',
                             `student_number` int(11) DEFAULT '0' COMMENT '学号',
                             `email` varchar(40) DEFAULT '' COMMENT 'email',
                             `register_datetime` datetime DEFAULT NULL COMMENT '注册日期',
                             `permission` tinyint(4) NOT NULL DEFAULT '1' COMMENT '权限等级',
                             `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态',
                             PRIMARY KEY (`user_id`),
                             UNIQUE KEY `name_passwd` (`name`,`password`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `bus_schedule` (
                                `schedule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '时间表id',
                                `start_station_id` int(11) NOT NULL COMMENT '起始车站',
                                `end_station_id` int(11) NOT NULL COMMENT '终点车站',
                                `departure_time` datetime NOT NULL COMMENT '出发时间',
                                `total_seat` int(11) NOT NULL COMMENT '总座位',
                                `last_seat` int(11) NOT NULL COMMENT '剩余座位',
                                `status` tinyint(4) NOT NULL COMMENT '班次状态',
                                PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `station_info` (
                                `station_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '车站id',
                                `station_name` varchar(20) NOT NULL DEFAULT '' COMMENT '车站名字',
                                `station_telephone_numb` varchar(20) DEFAULT '' COMMENT '车站联系人电话',
                                `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除状态',
                                PRIMARY KEY (`station_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `reserve_info` (
                                `reserve_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '预约id',
                                `user_id` int(11) NOT NULL COMMENT '用户id',
                                `schedule_id` int(11) NOT NULL COMMENT '校车发车id',
                                `reserve_time` datetime NOT NULL COMMENT '预定时间',
                                `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态',
                                PRIMARY KEY (`reserve_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `notice_info` (
                               `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告id',
                               `title` int(11) NOT NULL COMMENT '标题',
                               `content` text NOT NULL COMMENT '内容',
                               `sender_id` int(11) NOT NULL COMMENT '发送公告用户的id',
                               `create_time` datetime NOT NULL COMMENT '公告发送时间',
                               `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态',
                               PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `message_info` (
                                `message_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言消息id',
                                `content` text COMMENT '留言内容',
                                `user_id` int(11) DEFAULT NULL COMMENT '留言用户id',
                                `message_time` datetime DEFAULT NULL COMMENT '留言发送时间',
                                `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态',
                                PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;