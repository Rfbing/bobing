package com.piggysoft.bobing.api.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
import com.piggysoft.bobing.Constant.BobinScoresDef;
import com.piggysoft.bobing.Constant.GlobalMap;
import com.piggysoft.bobing.api.Utils.BobingToolUtils;
import com.piggysoft.bobing.entity.Code;
import com.piggysoft.bobing.entity.DiceCount;
import com.piggysoft.bobing.entity.DiceResult;
import com.piggysoft.bobing.entity.ImageAndMusicFile;
import com.piggysoft.bobing.entity.User;
import com.piggysoft.bobing.entity.WxSessionCode;
import com.piggysoft.bobing.entity.WxToken;
import com.piggysoft.bobing.jpa.GetUserDataDao;

@RestController
@RequestMapping("/api")
public class Bobing {
	private static Logger LOGGER = LoggerFactory.getLogger(Bobing.class);
	private String SERVER_ERROR_STRING = "400\r\nthe page is missing";
	private int RANDOM_STRING_LEN = 8;
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
		String sessionCodeStr = sessionCode.getSession_key();
		String tokenStr = BobingToolUtils.generateString(new Random(System.currentTimeMillis()), RANDOM_STRING_LEN);
		
		GlobalMap.userSessionkeyMap.put(tokenStr, sessionCodeStr);
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
	@RequestMapping(value = "/account/updateInfo", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void updateInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody String userInfoDataStr) {
		
		PrintWriter writer = null;
		try {
			writer = response.getWriter();			
			Gson gson = new Gson();
			User userDto = gson.fromJson(userInfoDataStr, User.class);
			GetUserDataDao getUserDao = new GetUserDataDao();
			String token = request.getHeader("token");
			if (userDto == null || token == null) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer.append(SERVER_ERROR_STRING);
				return;
			}
			// check token in GlobalMap.userSessionkeyMap
			String session_key = GlobalMap.userSessionkeyMap.get(token);
			if (session_key == null) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer.append(SERVER_ERROR_STRING);
				return;
			}
			getUserDao.storeUser(userDto.getNickName(), userDto.getAvatarUrl(),
					userDto.getGender(), userDto.getCity());
			response.setStatus(HttpStatus.OK.value());
			
			return;
		}catch (IOException exp) {
			LOGGER.error(Arrays.toString(exp.getStackTrace()));
			response.setStatus(HttpStatus.BAD_REQUEST.value());

			return;
		}
	}
	
	/* 获得可博饼次数 */
	// /api/bobing/getDice
	// DiceCount
	@RequestMapping(value = "/bobing/getDice", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void getHaveBobingTimes(HttpServletRequest request, HttpServletResponse response,@RequestBody String userInfoDataStr) {
		
		PrintWriter writer = null;
		try {
			writer = response.getWriter();			
			Gson gson = new Gson();
			User userDto = gson.fromJson(userInfoDataStr, User.class);
			GetUserDataDao getUserDao = new GetUserDataDao();
			String token = request.getHeader("token");
			if (userDto == null || token == null) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer.append(SERVER_ERROR_STRING);
				return;
			}
			// check token in GlobalMap.userSessionkeyMap
			String session_key = GlobalMap.userSessionkeyMap.get(token);
			if (session_key == null) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer.append(SERVER_ERROR_STRING);
				return;
			}
			int legalTimes = getUserDao.queryLegalBobingTimes(userDto.getNickName());
			DiceCount diceCount = new DiceCount();
			diceCount.setDice(legalTimes);
			String diceTimesJson = gson.toJson(diceCount);
			response.setStatus(HttpStatus.OK.value());
			writer.append(diceTimesJson);
			return;
		}catch (IOException exp) {
			LOGGER.error(Arrays.toString(exp.getStackTrace()));
			response.setStatus(HttpStatus.BAD_REQUEST.value());

			return;
		}
	}
	
	/* 博饼投点 */
	// /api/bobing/dice
	// DiceResult
	@RequestMapping(value = "/bobing/dice", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void playDice(HttpServletRequest request, HttpServletResponse response,@RequestBody String userInfoDataStr) {
		
		PrintWriter writer = null;
		try {
			writer = response.getWriter();			
			Gson gson = new Gson();
			User userDto = gson.fromJson(userInfoDataStr, User.class);
			GetUserDataDao getUserDao = new GetUserDataDao();
			String token = request.getHeader("token");
			if (userDto == null || token == null) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer.append(SERVER_ERROR_STRING);
				return;
			}
			// check token in GlobalMap.userSessionkeyMap
			String session_key = GlobalMap.userSessionkeyMap.get(token);
			if (session_key == null) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				writer.append(SERVER_ERROR_STRING);
				return;
			}
			int legalTimes = getUserDao.queryLegalBobingTimes(userDto.getNickName());
			//play dice
			if (legalTimes > 0) {
				int []diceVars = new int[6];
				int []result = new int[6];
				for (int i = 0; i < diceVars.length; i++) {
					 Random r = new Random();
					 // bobing 1,2,3,4,5,6 点数
				     int n2 = r.nextInt(6) + 1;
					diceVars[i] = n2;
					result[n2-1]++;
				}
				DiceResult diceResult = clacResult(result);
				diceResult.setDice(diceVars);
				String resultJsonStr = gson.toJson(diceResult);
				response.setStatus(HttpStatus.OK.value());
				writer.append(resultJsonStr);
				return;
			}
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			writer.append(SERVER_ERROR_STRING);			
			return;
		}catch (IOException exp) {
			LOGGER.error(Arrays.toString(exp.getStackTrace()));
			response.setStatus(HttpStatus.BAD_REQUEST.value());

			return;
		}
	}
	/** 计算结果 **/
	private DiceResult clacResult(int []result) {	
//		int[] result = {0,4,0,0,0,0};//用来测试逻辑
		String desc;
		int scores = 0;
		DiceResult diceResult = new DiceResult();
		if(result[3] == 6) {			
			desc = "六勃红";
			scores = BobinScoresDef.ALL_FOUR;
		}else if(result[3] == 5) {			
			desc = "五红";
			scores = BobinScoresDef.FIVE_FOUR;
		}else if(result[3] == 4) {
			if(result[0] == 2 && result[1] == 0 && result[2] == 0 && result[4] == 0 && result[5] == 0){				
				desc = "状元插金花";
				scores = BobinScoresDef.FOUR_FOUR_TWO_ONE;
			}
			else {				
				desc = "四点红";
				scores = BobinScoresDef.FOUR_FOUR;
			}
		}else if(result[3] == 3) {			
			desc = "三红";
			scores = BobinScoresDef.THREE_FOUR;
		}else  if(result[0] == 6) {
			desc = "遍地锦";
			scores = BobinScoresDef.ALL_ONE;
		}else if(result[1] == 6 || result[2] == 6 || result[4] == 6 || result[5] == 6) {			
			desc = "六勃黑";
			scores = BobinScoresDef.BLACK_SIX;
		}else  if(result[0] == 5 || result[1] == 5 || result[2] == 5 || result[4] == 5 || result[5] == 5) {
			if(result[3] == 1) {				
				desc = "五子带一秀";
				scores = BobinScoresDef.FIVE_STAGE;
			}else {				
				desc = "五子登科";
				scores = BobinScoresDef.FIVE_STAGE;
			}
		} else if(result[0]==1 && result[1]==1 && result[2]==1 && result[3]==1 && result[4]==1 && result[5]==1) {			
			desc = "对堂";
			scores = BobinScoresDef.ONE_TO_SIX;
		}else if(result[0] == 4 || result[1] == 4 || result[2] == 4 || result[4] == 4 || result[5] == 4) {			
			desc = "四进";
			scores = BobinScoresDef.FOUR_OR;
		}else if(result[3] == 2 && (result[0] != 4 || result[1] != 4 || result[2] != 4 || result[4] != 4 || result[5] != 4)) {
			
			desc = "二举";
			scores = BobinScoresDef.TWO_FOUR;
		}else if(result[3] == 1 && (result[0] != 4 || result[1] != 4 || result[2] != 4 || result[4] != 4 || result[5] != 4)) {
			
			desc = "一秀";
			scores = BobinScoresDef.ONE_FOUR;
		}else {
			desc = "什么也没有！";
		}
		diceResult.getResult().setName(desc);
		diceResult.getResult().setScore(scores);
		return diceResult;
	}
	/* 获取可分享次数 */
	// /api/bobing/getShare
	
	/* 获得排行榜 */
	// /api/bobing/rank
	
	/* 获得自身排行 */
	// /api/bobing/getSelfRank
	
	

}
