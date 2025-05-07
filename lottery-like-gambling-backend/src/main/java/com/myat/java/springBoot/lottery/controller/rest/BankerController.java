package com.myat.java.springBoot.lottery.controller.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myat.java.springBoot.lottery.dto.BankerDto;
import com.myat.java.springBoot.lottery.exception.BankerNotFoundException;
import com.myat.java.springBoot.lottery.exception.MiddlemanNotFoundException;
import com.myat.java.springBoot.lottery.model.Banker;
import com.myat.java.springBoot.lottery.response.*;
import com.myat.java.springBoot.lottery.service.AuthService;
import com.myat.java.springBoot.lottery.service.BankerService;

import jakarta.validation.Validator;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/bankers")
public class BankerController {
	
	@Autowired
	BankerService bankerService;
	
	@Autowired
	AuthService authService;

	@Autowired
	Validator validation;
	
	ModelMapper modelMapper = new ModelMapper();
	
	@GetMapping
	public Mono<ResponseEntity<ApiResponse>> getAllBankers() {
	    return this.bankerService.getAllBankers()
	            .collectList() 
	            .flatMap(bankers -> {
	                if (bankers.isEmpty()) {
	                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
	                            .body(ApiResponse.error(404, "Bankers not found.", "No bankers available")));
	                }
	                return Mono.just(ResponseEntity.ok(ApiResponse.success("All bankers retrieved successfully.", 200, bankers)));
	            })
	            .onErrorResume(BankerNotFoundException.class, err -> 
	                Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body(ApiResponse.error(404, "Bankers not found.", err.getMessage())))
	            )
	            .onErrorResume(Exception.class, err -> 
	                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage())))
	            );
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<ApiResponse>> getBankerById(@PathVariable String id){
		
		return this.bankerService.getBankerById(id)
				.map(banker -> {
					return ResponseEntity.ok().body(ApiResponse.success("Banker retrieved successfully by id.", 200, banker));
				})
				.onErrorResume(BankerNotFoundException.class,err -> 
					Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "Banker not found with given id.", err.getMessage())))
				);
		
	}
	
	@GetMapping("/limit")
	public Mono<ResponseEntity<ApiResponse>> getBankersWithLimit(
			@RequestParam(value = "page", defaultValue = "0") int page,
		    @RequestParam(value = "size", defaultValue = "10") int size){
		return this.bankerService.getBankersWithLimit(page, size)
				.collectList()
				.flatMap(bankers -> {
	                if (bankers.isEmpty()) {
	                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
	                            .body(ApiResponse.error(404, "Bankers not found.", "No bankers available")));
	                }
	                return Mono.just(ResponseEntity.ok(ApiResponse.success("All bankers retrieved successfully.", 200, bankers)));
	            })
	            .onErrorResume(BankerNotFoundException.class, err -> 
	                Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body(ApiResponse.error(404, "Bankers not found.", err.getMessage())))
	            )
	            .onErrorResume(Exception.class, err -> 
	                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage())))
	            );
	}
	
	@PostMapping("/save")
	public Mono<ResponseEntity<ApiResponse>> saveBanker(@RequestBody Banker banker){
		
		if (!this.validation.validate(banker).isEmpty()) {
          return Mono.error(new RuntimeException("Bad request"));
      }
      return this.authService.registerBanker(banker)
      		.flatMap(data ->
      				Mono.just(ResponseEntity.ok()
                              .body(ApiResponse.success("register success", 200, data)))
      				)
      		.onErrorResume(RuntimeException.class, err-> {
				return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "bad request", err.getMessage())));
			});
	}
	
	@DeleteMapping("/{bankerId}")
	public Mono<ResponseEntity<ApiResponse>> removeMiddleman(@PathVariable String bankerId) {
		System.out.println("Banker Controller.....");
		return this.bankerService.removeBanker(bankerId)
				.flatMap(data -> {
					System.out.println("banker data " + data.getUsername());
					return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED)
							.body(ApiResponse.success("Banker is deleted successfully", 202, data)));
				})
				.onErrorResume(BankerNotFoundException.class, err -> {
					return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(ApiResponse.error(400, "bad request", err.getMessage())));
				})
				.onErrorResume(Exception.class, err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage()))));
	}
	

	
}
