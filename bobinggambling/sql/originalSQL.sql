create database bobingtest;
drop table t_user;
CREATE TABLE `t_user` (
	`user_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`nickName` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`avatarUrl` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`gender` INT(4)  DEFAULT 1,
	`city` VARCHAR(30) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
	`haveBobingTimes` SMALLINT  DEFAULT 5,
	`usedBobingTimes` SMALLINT  DEFAULT 0,
	`scores` SMALLINT  DEFAULT 0,
	`bobingDescriptions` mediumtext 
	PRIMARY KEY (`user_id`)
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;
##INSERT INTO `jpasample`.`t_user` (`user_id`, `nickname`, `real_name`, `phone_num1`, `phone_num2`, `phone_num3`, `sexy`, `born_date`, `age`, `job`, `born_address`, `now_address`, `credit_score`, `id_card_num`, `to_receive_money`, `owe_money`, `register_date`, `last_logon_date`, `email`, `password`) VALUES ('2', 'testuser1', 'user1RealName', '17712344321', '17745677654', '17798769876', '男', '1984-02-14', '18', '建筑', '浙江', '浙江', '100', '330000198402142830', '0', '0', '2018-02-28', '2018-02-28', 'test@163.com', '123456');
##INSERT INTO `jpasample`.`t_user` (`user_id`, `nickname`, `real_name`, `phone_num1`, `phone_num2`, `born_date`, `age`, `job`, `born_address`, `now_address`, `id_card_num`, `to_receive_money`, `owe_money`, `register_date`, `last_logon_date`, `email`, `password`) VALUES ('3', 'testuser2', 'user2RealName', '17765486548', '17785958595', '1980-01-11 17:20:16', '38', '建筑', '福建', '福建', '350000198001119174', '0', '0', '2018-02-28 17:22:09', '2018-02-28 17:22:16', 'test2@163.com', '123456');
##INSERT INTO `jpasample`.`t_user` (`user_id`, `nickname`, `real_name`, `phone_num1`, `phone_num2`, `born_date`, `age`, `job`, `born_address`, `now_address`, `id_card_num`, `to_receive_money`, `owe_money`, `register_date`, `last_logon_date`, `email`, `password`) VALUES ('4', 'testuser3', 'user3RealName', '13636363636', '13637373737', '1985-05-24 17:32:12', '33', '建筑', '福建', '福建', '350000198505243312', '0', '0', '2018-02-28 17:33:05', '2018-02-28 17:33:07', 'test3@163.com', '123456');
##INSERT INTO `jpasample`.`t_user` (`user_id`, `nickname`, `real_name`, `phone_num1`, `phone_num2`, `born_date`, `age`, `job`, `born_address`, `now_address`, `id_card_num`, `to_receive_money`, `owe_money`, `register_date`, `last_logon_date`, `email`, `password`) VALUES ('5', 'testuser4', 'user4RealName', '13737373737', '13838383838', '1984-04-25 17:34:35', '34', '建筑', '福建', '福建', '350000198404254119', '0', '0', '2018-02-28 17:35:37', '2018-02-28 17:35:38', 'test4@163.com', '123456');


##Select *,(@rowNum:=@rowNum+1) as rowNo From t_user, (Select (@rowNum :=0) ) b Order by t_user.scores 