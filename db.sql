CREATE TABLE `user_info` (
                             `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户的id',
                             `name` varchar(20) NOT NULL COMMENT '登录的用户名',
                             `password` varchar(20) NOT NULL DEFAULT '' COMMENT '密码',
                             `nickname` varchar(20) NOT NULL DEFAULT '' COMMENT '昵称',
                             `student_number` int(11) DEFAULT '0' COMMENT '学号',
                             `phone_numb` varchar(20) DEFAULT '' COMMENT '电话号码',
                             `email` varchar(40) DEFAULT '' COMMENT 'email',
                             `register_datetime` datetime DEFAULT NULL COMMENT '注册日期',
                             `permission` tinyint(4) NOT NULL DEFAULT '1' COMMENT '权限等级',
                             `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否被删除',
                             PRIMARY KEY (`user_id`),
                             UNIQUE KEY `name_passwd` (`name`,`password`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `bus_schedule` (
                                `schedule_id` int NOT NULL AUTO_INCREMENT,
                                `start_station_id` int NOT NULL COMMENT '起始车站',
                                `end_station_id` int NOT NULL COMMENT '终点车站',
                                `departure_time` datetime NOT NULL COMMENT '出发时间',
                                `last_seat` int NOT NULL COMMENT '剩余座位',
                                PRIMARY KEY (`schedule_id`)
) ENGINE = innodb DEFAULT CHARACTER SET = "utf8mb4" COLLATE = "utf8mb4_general_ci";

CREATE TABLE `station_info` (
                                `station_id` int(11) NOT NULL AUTO_INCREMENT,
                                `station_name` varchar(20) NOT NULL DEFAULT '' COMMENT '车站名字',
                                `station_address` varchar(50) NOT NULL DEFAULT '' COMMENT '车站地址',
                                `station_telephone_numb` varchar(20) DEFAULT '' COMMENT '车站联系人电话',
                                PRIMARY KEY (`station_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `reserve_info` (
                                `reserve_id` int NOT NULL AUTO_INCREMENT,
                                `user_id` int NOT NULL COMMENT '用户id',
                                `schedule_id` int NOT NULL COMMENT '校车发车id',
                                `reserve_time` datetime NOT NULL COMMENT '预定时间',
                                PRIMARY KEY (`reserve_id`)
) ENGINE = innodb DEFAULT CHARACTER SET = "utf8mb4" COLLATE = "utf8mb4_general_ci";

CREATE TABLE `notice_info` (
                               `notice_id` int NOT NULL AUTO_INCREMENT,
                               `content` text NOT NULL COMMENT '内容',
                               `sender_id` int NOT NULL COMMENT '发送公告用户的id',
                               `create_time` datetime NOT NULL COMMENT '公告发送时间',
                               PRIMARY KEY (`notice_id`)
) ENGINE = innodb DEFAULT CHARACTER SET = "utf8mb4" COLLATE = "utf8mb4_general_ci";

CREATE TABLE `message_info` (
                                `message_id` int NOT NULL AUTO_INCREMENT,
                                `content` text NULL COMMENT '留言内容',
                                `user_id` int NULL COMMENT '留言用户id',
                                `message_time` datetime NULL COMMENT '留言发送时间',
                                PRIMARY KEY (`message_id`)
) ENGINE = innodb DEFAULT CHARACTER SET = "utf8mb4" COLLATE = "utf8mb4_general_ci";