package com.piggysoft.bobing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserRank {
	@Column(name="user_id")
    int userId;
	 // 虚拟列，用sql语句在查询时得到rank榜单
    @Column(name="rankNum")
    int rankNum;
}
