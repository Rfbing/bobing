package com.piggysoft.bobing.entity;

public class WxSessionCode {
	String openid;// 	string 	用户唯一标识 	
	String session_key;// 	string 	会话密钥 	
	String uinionid;// 	string 	用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UinionID 机制说明。 	
	int errcode;// 	number 	错误码 	
	String errMsg;// 	string 	错误信息
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getSession_key() {
		return session_key;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}
	public String getUinionid() {
		return uinionid;
	}
	public void setUinionid(String uinionid) {
		this.uinionid = uinionid;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
}
