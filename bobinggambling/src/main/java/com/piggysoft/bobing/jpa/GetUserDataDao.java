package com.piggysoft.bobing.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.piggysoft.bobing.entity.User;

public class GetUserDataDao {
	@Autowired
    private UserRepository userRepository;
    Page<User> getTopSocresUsers(){
    	Sort sort = new Sort(Sort.Direction.DESC, "scores");
    	int ps = 100;    	
    	return userRepository.findAll(PageRequest.of(0, ps, sort));
    }
}
