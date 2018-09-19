package com.piggysoft.bobing.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.piggysoft.bobing.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	// List<User> findAllByOrderByScoresAtDesc();
	User findByNickName(String nickName);
//	@Modifying(clearAutomatically = true) 
//	@Query("update User a set a.scores =:scores where a.nickName =:nickName")  
//	public void updateScoresAndDes(@Param("scores") int scores,
//			@Param("bobingDescriptions") String bobingDescriptions,
//			@Param("nickName")String nickName)
//	
//	@Modifying(clearAutomatically = true) 
//	@Query("update User a set a.scores =:scores where a.nickName =:nickName")  
//	public void updateBoBingTimes(@Param("haveBobingTimes") int haveBobingTimes,
//			@Param("usedBobingTimes") int usedBobingTimes,
//			@Param("nickName")String nickName)
//	
//	
//	@Modifying(clearAutomatically = true) 
//	@Query("update User a set a.scores =:scores where a.nickName =:nickName")  
//	public void updateScores(@Param("scores") int scores,@Param("nickName")String nickName)
}
