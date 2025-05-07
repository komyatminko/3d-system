package com.myat.java.springBoot.lottery.service;

import org.springframework.http.ResponseEntity;

import com.myat.java.springBoot.lottery.dto.BankerDto;
import com.myat.java.springBoot.lottery.dto.MiddleManDto;
import com.myat.java.springBoot.lottery.dto.UserDto;
import com.myat.java.springBoot.lottery.model.Banker;
import com.myat.java.springBoot.lottery.model.JWTToken;
import com.myat.java.springBoot.lottery.model.MiddleMan;
import com.myat.java.springBoot.lottery.model.User;

import reactor.core.publisher.Mono;


public interface AuthService {
	Mono<JWTToken> registerUser(User user);
	Mono<BankerDto> registerBanker(Banker banker);
	Mono<MiddleManDto> registerMiddleMan(MiddleMan middleman);
	Mono<JWTToken> login(User user);
	Mono<UserDto> getUserFromToken(String token);
	
}
