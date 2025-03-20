package com.myat.java.springBoot.lottery.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.myat.java.springBoot.lottery.model.MiddleMan;

public interface MiddleManDao extends ReactiveMongoRepository<MiddleMan, String>{

}
