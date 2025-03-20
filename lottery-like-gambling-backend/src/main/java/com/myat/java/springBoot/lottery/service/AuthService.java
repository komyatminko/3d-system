package com.myat.java.springBoot.lottery.service;

import org.springframework.http.ResponseEntity;

import com.myat.java.springBoot.lottery.dto.UserDto;
import com.myat.java.springBoot.lottery.model.JWTToken;
import com.myat.java.springBoot.lottery.model.User;

import reactor.core.publisher.Mono;


public interface AuthService {
	Mono<JWTToken> register(User user);
	Mono<JWTToken> login(User user);
	Mono<UserDto> getUserFromToken(String token);
	
}
