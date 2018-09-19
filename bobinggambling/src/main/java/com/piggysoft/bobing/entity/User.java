package com.piggysoft.bobing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_user")
public class User {
	 private static final long serialVersionUID = 1L;

	 	@GeneratedValue(strategy = GenerationType.AUTO)
	    @Id
	    @Column(name="user_id")
	    int userId;
	    @Column(name="nickName")
	    String nickName;
	    @Column(name="avatarUrl")
	    String avatarUrl;
	    @Column(name="gender")
	    int    gender;
	    @Column(name="city")
	    String city;
	    @Column(name="haveBobingTimes")
	    int haveBobingTimes;
	    @Column(name="usedBobingTimes")
	    int usedBobingTimes;
	    @Column(name="scores")
	    int scores;
	    @Column(name="bobingDescriptions")
	    String bobingDescriptions;
	   
	    
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getAvatarUrl() {
			return avatarUrl;
		}
		public void setAvatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
		}
		public int getGender() {
			return gender;
		}
		public void setGender(int gender) {
			this.gender = gender;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public int getHaveBobingTimes() {
			return haveBobingTimes;
		}
		public void setHaveBobingTimes(int haveBobingTimes) {
			this.haveBobingTimes = haveBobingTimes;
		}
		public int getUsedBobingTimes() {
			return usedBobingTimes;
		}
		public void setUsedBobingTimes(int usedBobingTimes) {
			this.usedBobingTimes = usedBobingTimes;
		}
		public int getScores() {
			return scores;
		}
		public void setScores(int scores) {
			this.scores = scores;
		}
		public String getBobingDescriptions() {
			return bobingDescriptions;
		}
		public void setBobingDescriptions(String bobingDescriptions) {
			this.bobingDescriptions = bobingDescriptions;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	    
	    
	    
}
