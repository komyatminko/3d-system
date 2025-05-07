package com.myat.java.springBoot.lottery.service;

import com.myat.java.springBoot.lottery.dto.BankerDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankerService {

	public Flux<BankerDto> getAllBankers();
	public Mono<BankerDto> getBankerById(String bankerId);
	public Flux<BankerDto> getBankersWithLimit(int page, int size);
	public Mono<BankerDto> removeBanker(String bankerId);
}
