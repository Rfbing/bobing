package com.piggysoft.bobing.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.Jsr310Converters.StringToDurationConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.piggysoft.bobing.entity.User;

import javax.validation.constraints.Null;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
public class GetUserDataDao {
	public static int ps = 100;
	public static int defaultBobingTimes = 5;
	public static int defaultUsedBobingTimes = 0;
	@Autowired
    private UserRepository userRepository;
    Page<User> getTopSocresUsers(){
    	Sort sort = new Sort(Sort.Direction.DESC, "scores");
    	   	
    	return userRepository.findAll(PageRequest.of(0, ps, sort));
    }
    /**
     * store a user by nickname and avatarUrl
     * @param nickName
     * @param avatarUrl
     * @return storeUser(user)
     */
    boolean storeUser(String nickName, String avatarUrl) {
    	User user = new User();
    	user.setNickName(nickName);
    	user.setAvatarUrl(avatarUrl);
    	user.setBobingDescriptions("还未开始游戏");
    	user.setCity("默认");    	
    	user.setHaveBobingTimes(defaultBobingTimes);
    	user.setScores(0);
    	user.setUsedBobingTimes(defaultUsedBobingTimes);
    	user.setGender(1);
    	return storeUser(user);
    }
    /**
     *  store a user
     * @param user
     * @return true if succ,or false otherwise
     */
    boolean storeUser(User user) {
    	if (user == null) {
			return false;
		}
    	User userSave = userRepository.save(user);
    	if (userSave == null || userSave.getNickName().equalsIgnoreCase(user.getNickName()) == false) {
			return false;
		}
    	return true;
    }
    
    User findUserByNickName(String nickName) {
    	if (StringUtils.isEmpty(nickName)) {
			return null;
		}    	
    	return userRepository.findByNickName(nickName);
    }
    /**
     * update scores and description
     * @param nickName
     * @param scores
     * @param descriptions
     * @return false or true 
     */
    boolean updateUserInfo(String nickName, int scores, String descriptions) {
    	User userOld = findUserByNickName(nickName);
    	if (userOld == null) {
    		return false;	
		}
    	int oldScores = userOld.getScores();
    	String oldDes = userOld.getBobingDescriptions();
    	int newScores = oldScores + scores;
    	if (newScores > 1<<20) {
			return false;
		}
    	userOld.setScores(newScores);
    	userOld.setBobingDescriptions(oldDes + "\r\n" + descriptions);
    	userOld.setUsedBobingTimes(userOld.getUsedBobingTimes() + 1);
    	return storeUser(userOld);
    }
    /**
     *  add addHaveTimes to haveBobingTimes of user
     * @param nickName
     * @param addHaveTimes
     * @return
     */
    boolean addUserBoBingTimes(String nickName, int addHaveTimes) {
    	User userOld = findUserByNickName(nickName);
    	if (userOld == null) {
    		return false;	
		}
    	int oldHaveTimes = userOld.getHaveBobingTimes();
    	userOld.setHaveBobingTimes(oldHaveTimes + addHaveTimes);
    	return storeUser(userOld);
    }
    /**
     * query HaveBobingTimes - UsedBobingTimes
     * @param nickName
     * @return
     */
    int queryLegalBobingTimes(String nickName) {
    	User userOld = findUserByNickName(nickName);
    	if (userOld == null) {
    		return 0;	
		}
    	return userOld.getHaveBobingTimes() - userOld.getUsedBobingTimes();    	
    }
}
