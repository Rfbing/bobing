package com.piggysoft.bobing.api.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.piggysoft.bobing.Constant.GlobalMap;
import com.piggysoft.bobing.entity.Code;
import com.piggysoft.bobing.entity.ImageAndMusicFile;
import com.piggysoft.bobing.entity.WxSessionCode;
import com.piggysoft.bobing.entity.WxToken;

@RestController
@RequestMapping("/api")
public class Bobing {

	private String SERVER_ERROR_STRING = "400\r\nthe page is missing";
	private static Logger LOGGER = LoggerFactory.getLogger(Bobing.class);
	@Value("${wxTokenPath}")
	private String wxTokenPath;
	@Value("${backgroundImage}")
	private String backgroundImage;
	@Value("${backgroundMusic}")
	private String backgroundMusic;
	/**
	 * 获取token
	 * @param request
	 * @param response
	 * @param cmddata
	 * @return token
	 */
	@RequestMapping(value = "/account/wxLogin", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void getWxLoginToken(HttpServletRequest request, HttpServletResponse response,
			 @RequestBody String codeData) {
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		Gson gson = new Gson();
		Code code = gson.fromJson(codeData, Code.class);
		if (code == null || StringUtils.isEmpty(code.getCode())) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			writer.append(SERVER_ERROR_STRING);
			return;
		}
		// 向微信请求session_key
		String requestStr = wxTokenPath.replace("JSCODE",code.getCode());
		Client clientJersey = ClientBuilder.newClient();
		WebTarget webTarget = clientJersey.target(requestStr);
		 
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response jserseyResp = invocationBuilder.get();
		if (jserseyResp.getStatus() != 200 ) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			writer.append(SERVER_ERROR_STRING);
			return;
		}
		String wxData = jserseyResp.getEntity().toString();
		WxSessionCode sessionCode = gson.fromJson(wxData, WxSessionCode.class);
		WxToken wxToken = new WxToken();
		String tokenStr = sessionCode.getSession_key();
		//GlobalMap.userSessionkeyMap.put(key, value);
		wxToken.setToken(tokenStr);
		String tokenJsonStr = gson.toJson(wxToken);
		//将微信给点session key传送回微信小程序界面js
		writer.append(tokenJsonStr);
		response.setStatus(HttpStatus.OK.value());
		return ;
		}catch (IOException exp) {
			LOGGER.error(Arrays.toString(exp.getStackTrace()));
			response.setStatus(HttpStatus.BAD_REQUEST.value());

			return;
		}
	}
	
	@RequestMapping(value = "/bobing/getFile", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void getFileUrlPath(HttpServletRequest request, HttpServletResponse response) {
		
		ImageAndMusicFile imageAndMusicFile = new ImageAndMusicFile();
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			if(StringUtils.isEmpty(backgroundMusic) || StringUtils.isEmpty(backgroundImage)) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				return;
			}
			imageAndMusicFile.setBackground_music(backgroundMusic);
			imageAndMusicFile.setIndex_img(backgroundImage);
			Gson gson = new Gson();
			String fileStr = gson.toJson(imageAndMusicFile);
			writer.append(fileStr);
			response.setStatus(HttpStatus.OK.value());

			return;
		}catch (IOException exp) {
			LOGGER.error(Arrays.toString(exp.getStackTrace()));
			response.setStatus(HttpStatus.BAD_REQUEST.value());

			return;
		}
	}
	
	/* 获得可博饼次数  ,更新用户信息，存入数据库
	* 在map中绑定用户昵称和session_key，即头部的token字段
	*  */
	// /api/account/updateInfo
	
	
	/* 获得可博饼次数 */
	// /api/bobing/getDice
	// DiceCount
	
	
	/* 博饼投点 */
	// /api/bobing/dice
	// DiceResult
	
	/* 获取可分享次数 */
	// /api/bobing/getShare
	
	/* 获得排行榜 */
	// /api/bobing/rank
	
	/* 获得自身排行 */
	// /api/bobing/getSelfRank
	
	

}
