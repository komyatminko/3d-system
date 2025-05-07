package com.myat.java.springBoot.lottery.service;

import com.myat.java.springBoot.lottery.dto.MiddleManDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MiddleManService {
	public Flux<MiddleManDto> getAllMiddleMen();
	public Mono<MiddleManDto> getMiddleManById(String middlemanId);
	public Flux<MiddleManDto> getMiddleMenWithLimit(int page, int size);
	public Mono<MiddleManDto> updateMiddleManById(MiddleManDto middlemanDto, String middlemanId);
	public Mono<MiddleManDto> removeMiddleMan(String middlemanId);
	public Flux<MiddleManDto> removeMiddleManByBankerId(String bankerId);
}
