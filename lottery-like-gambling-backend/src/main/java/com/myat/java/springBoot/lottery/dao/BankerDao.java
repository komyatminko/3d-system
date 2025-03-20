package com.myat.java.springBoot.lottery.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.myat.java.springBoot.lottery.model.Banker;

public interface BankerDao extends ReactiveMongoRepository<Banker, String>{

}
