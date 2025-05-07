package com.myat.java.springBoot.lottery.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myat.java.springBoot.lottery.dao.BankerDao;
import com.myat.java.springBoot.lottery.dao.MiddleManDao;
import com.myat.java.springBoot.lottery.dto.BankerDto;
import com.myat.java.springBoot.lottery.dto.MiddleManDto;
import com.myat.java.springBoot.lottery.exception.BankerNotFoundException;
import com.myat.java.springBoot.lottery.exception.MiddlemanNotFoundException;
import com.myat.java.springBoot.lottery.service.MiddleManService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MiddleManServiceImpl implements MiddleManService{

	@Autowired
	MiddleManDao middleManDao;
	
	ModelMapper modelMapper = new ModelMapper();

	@Override
	public Flux<MiddleManDto> getAllMiddleMen() {
		
		return this.middleManDao.findAll()
				.switchIfEmpty(Mono.error(new MiddlemanNotFoundException("Banker not found.")))
				.map(middlemen-> modelMapper.map(middlemen, MiddleManDto.class));
	}

	@Override
	public Mono<MiddleManDto> getMiddleManById(String middlemanId) {
		
		return this.middleManDao.findById(middlemanId)
				.switchIfEmpty(Mono.error(new MiddlemanNotFoundException("Banker not found.")))
				.map(middleman -> modelMapper.map(middleman, MiddleManDto.class));
	}

	@Override
	public Flux<MiddleManDto> getMiddleMenWithLimit(int page, int size) {
		int skip = page * size; 
	    return this.middleManDao.findAll()
	                         .skip(skip) 
	                         .take(size) 
	                         .map(middleman -> this.modelMapper.map(middleman, MiddleManDto.class)); 
	}

	@Override
	public Mono<MiddleManDto> removeMiddleMan(String middlemanId) {
	    return this.middleManDao.findById(middlemanId)
	        .switchIfEmpty(Mono.error(new MiddlemanNotFoundException("Middleman not found to delete.")))
	        .flatMap(middlemanEntity ->
	            this.middleManDao.deleteById(middlemanId)
	                .then(Mono.just(this.modelMapper.map(middlemanEntity, MiddleManDto.class)))
	        );
	}

	@Override
	public Flux<MiddleManDto> removeMiddleManByBankerId(String bankerId) {
		
		return this.middleManDao.findByBankerId(bankerId)
				.switchIfEmpty(Mono.error(new MiddlemanNotFoundException("Middleman not found with given banker id.")))
				.flatMap(middlemanEntity -> this.middleManDao.deleteById(middlemanEntity.getId())
						.then(Mono.just(this.modelMapper.map(middlemanEntity, MiddleManDto.class))))
				;
	}

	@Override
	public Mono<MiddleManDto> updateMiddleManById(MiddleManDto middlemanDto, String middlemanId) {
		
		if(middlemanDto.getId() != null) {
			return this.middleManDao.findById(middlemanId)
					.switchIfEmpty(Mono.error(new MiddlemanNotFoundException("Middleman not found to update.")))
					.flatMap(oldMiddleman -> {
						oldMiddleman.setUsername(middlemanDto.getUsername());
						oldMiddleman.setAddress(middlemanDto.getAddress());
						oldMiddleman.setBankerId(middlemanDto.getBankerId());
						oldMiddleman.setPhone(middlemanDto.getPhone());
						return this.middleManDao.save(oldMiddleman);
					})
					.map(savedMiddleman-> this.modelMapper.map(middlemanDto, MiddleManDto.class))
					;
		}else {
			return Mono.error(new RuntimeException("Middleman id is missing to update."));
		}
		
	}


}
