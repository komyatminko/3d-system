package com.myat.java.springBoot.lottery.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myat.java.springBoot.lottery.dao.BankerDao;
import com.myat.java.springBoot.lottery.dto.BankerDto;
import com.myat.java.springBoot.lottery.dto.MiddleManDto;
import com.myat.java.springBoot.lottery.exception.BankerNotFoundException;
import com.myat.java.springBoot.lottery.exception.MiddlemanNotFoundException;
import com.myat.java.springBoot.lottery.model.Banker;
import com.myat.java.springBoot.lottery.service.BankerService;
import com.myat.java.springBoot.lottery.service.MiddleManService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankerServiceImpl implements BankerService{
	
	@Autowired
	BankerDao bankerDao;
	
	@Autowired
	MiddleManService middlemanService;
	
	ModelMapper modelMapper = new ModelMapper();

	@Override
	public Flux<BankerDto> getAllBankers() {
		
		return this.bankerDao.findAll()
				.switchIfEmpty(Mono.error(new BankerNotFoundException("Banker not found.")))
				.map(bankers-> modelMapper.map(bankers, BankerDto.class));
	}

	@Override
	public Mono<BankerDto> getBankerById(String bankerId) {
		
		return this.bankerDao.findById(bankerId)
				.switchIfEmpty(Mono.error(new BankerNotFoundException("Banker not found.")))
				.map(banker -> modelMapper.map(banker, BankerDto.class));
	}

	@Override
	public Flux<BankerDto> getBankersWithLimit(int page, int size) {
		int skip = page * size; 
	    return this.bankerDao.findAll()
	                         .skip(skip) 
	                         .take(size) 
	                         .map(banker -> this.modelMapper.map(banker, BankerDto.class)); 
	}

	@Override
	public Mono<BankerDto> removeBanker(String bankerId) {

	    return this.bankerDao.findById(bankerId)
	        .switchIfEmpty(Mono.error(new BankerNotFoundException("Banker not found to delete.")))
	        .flatMap(bankerEntity ->
	            this.middlemanService.removeMiddleManByBankerId(bankerId) 
	            	.collectList()
	                .then() 
	                .then(this.bankerDao.deleteById(bankerId)) 
	                .thenReturn(this.modelMapper.map(bankerEntity, BankerDto.class)) 
	        );
	}



	

}
