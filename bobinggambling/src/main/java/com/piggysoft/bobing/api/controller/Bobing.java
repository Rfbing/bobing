package com.piggysoft.bobing.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Bobing {
	
	@RequestMapping("/account/wxLogin")
	public String getLoginToken() {
		
		return null;
	}
	
	
	@RequestMapping("/bobing/getFile")
	public String getFileUrlPath() {
		
		return null;
	}
	/* 获得可博饼次数 */
	// /api/bobing/getDice
	
	
	/* 博饼投点 */
	// /api/bobing/dice
	
	/* 获取可分享次数 */
	// /api/bobing/getShare
	
	/* 获得排行榜 */
	// /api/bobing/rank
	
	/* 获得自身排行 */
	// /api/bobing/getSelfRank
	
	/* 获得可博饼次数   */
	// /api/account/updateInfo
}
