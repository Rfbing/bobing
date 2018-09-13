package com.piggysoft.bobing.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.piggysoft.bobing.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	// List<User> findAllByOrderByScoresAtDesc();
	
}
