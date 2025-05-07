package com.myat.java.springBoot.lottery.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.myat.java.springBoot.lottery.model.Banker;
import com.myat.java.springBoot.lottery.model.User;

import reactor.core.publisher.Mono;

public interface BankerDao extends ReactiveMongoRepository<Banker, String>{
	Mono<User> findByUsername(String username);
}
