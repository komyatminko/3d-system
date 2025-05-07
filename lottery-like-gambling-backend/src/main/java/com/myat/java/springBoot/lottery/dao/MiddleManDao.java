package com.myat.java.springBoot.lottery.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.myat.java.springBoot.lottery.model.MiddleMan;
import com.myat.java.springBoot.lottery.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MiddleManDao extends ReactiveMongoRepository<MiddleMan, String>{
	Mono<User> findByUsername(String username);
	Flux<MiddleMan> findByBankerId(String bankerId);
}
