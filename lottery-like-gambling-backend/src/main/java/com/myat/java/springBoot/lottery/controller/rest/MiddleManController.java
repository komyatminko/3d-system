package com.myat.java.springBoot.lottery.controller.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myat.java.springBoot.lottery.dto.MiddleManDto;
import com.myat.java.springBoot.lottery.exception.BankerNotFoundException;
import com.myat.java.springBoot.lottery.exception.MiddlemanNotFoundException;
import com.myat.java.springBoot.lottery.model.Banker;
import com.myat.java.springBoot.lottery.model.MiddleMan;
import com.myat.java.springBoot.lottery.response.*;
import com.myat.java.springBoot.lottery.service.AuthService;
import com.myat.java.springBoot.lottery.service.BankerService;
import com.myat.java.springBoot.lottery.service.MiddleManService;

import jakarta.validation.Validator;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/v1/middlemen")
public class MiddleManController {

	@Autowired
	MiddleManService middleManService;

	@Autowired
	AuthService authService;

	@Autowired
	Validator validation;

	ModelMapper modelMapper = new ModelMapper();

	@GetMapping
	public Mono<ResponseEntity<ApiResponse>> getAllMiddleMen() {
		return this.middleManService.getAllMiddleMen().collectList().flatMap(middlemen -> {
			if (middlemen.isEmpty()) {
				return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error(404, "Middlemen not found.", "No middlemen available")));
			}
			return Mono.just(
					ResponseEntity.ok(ApiResponse.success("All middlemen retrieved successfully.", 200, middlemen)));
		}).onErrorResume(MiddlemanNotFoundException.class,
				err -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error(404, "Middlemen not found.", err.getMessage()))))
				.onErrorResume(Exception.class, err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage()))));
	}
	
//	@GetMapping
//	public Mono<ResponseEntity<ApiResponse>> getMiddleMenByBankerId(@RequestParam("bankerId") String bankerId){
//		return null;
//	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<ApiResponse>> getMiddleManById(@PathVariable String id) {

		return this.middleManService.getMiddleManById(id).map(middleman -> {
			return ResponseEntity.ok()
					.body(ApiResponse.success("Middleman retrieved successfully by id.", 200, middleman));
		}).onErrorResume(MiddlemanNotFoundException.class,
				err -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error(404, "Middleman not found with given id.", err.getMessage()))))
				.onErrorResume(Exception.class, err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage()))));

	}

	@GetMapping("/limit")
	public Mono<ResponseEntity<ApiResponse>> getMiddleMenWithLimit(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		return this.middleManService.getMiddleMenWithLimit(page, size).collectList().flatMap(middlemen -> {
			if (middlemen.isEmpty()) {
				return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error(404, "Middlemen not found.", "No middlemen available")));
			}
			return Mono.just(
					ResponseEntity.ok(ApiResponse.success("All middlemen retrieved successfully.", 200, middlemen)));
		}).onErrorResume(MiddlemanNotFoundException.class,
				err -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponse.error(404, "Middlemen not found.", err.getMessage()))))
				.onErrorResume(Exception.class, err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage()))));
	}

	@PostMapping("/save")
	public Mono<ResponseEntity<ApiResponse>> saveMiddleMan(@RequestBody MiddleMan middleman) {

		if (!this.validation.validate(middleman).isEmpty()) {
			return Mono.error(new RuntimeException("Bad request"));
		}
		return this.authService.registerMiddleMan(middleman)
				.flatMap(
						data -> Mono.just(ResponseEntity.ok().body(ApiResponse.success("register success", 200, data))))
				.onErrorResume(RuntimeException.class, err -> {
					return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(ApiResponse.error(400, "Request with different name.", err.getMessage())));
				});
	}
	
	@PutMapping("/{middlemanId}")
	public Mono<ResponseEntity<ApiResponse>> updateMiddleMan(
			@RequestBody MiddleManDto middlemanDto,
			@PathVariable String middlemanId
			){
		
		return this.middleManService.updateMiddleManById(middlemanDto, middlemanId)
				.flatMap( data -> Mono.just(ResponseEntity.ok().body(ApiResponse.success("Middleman is updated successfully.", 200, data))))
				.onErrorResume(MiddlemanNotFoundException.class, err -> {
					return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(ApiResponse.error(404, "Middleman not found.", err.getMessage())));
				})
				.onErrorResume(Exception.class, err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage()))))
				;
	}

	@DeleteMapping("/{middlemanId}")
	public Mono<ResponseEntity<ApiResponse>> removeMiddleman(@PathVariable String middlemanId) {
		return this.middleManService.removeMiddleMan(middlemanId)
				.flatMap(data -> {
					System.out.println("middle man data " + data.getUsername());
					return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED)
							.body(ApiResponse.success("Middleman is deleted successfully", 202, data)));
				})
				.onErrorResume(MiddlemanNotFoundException.class, err -> {
					return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(ApiResponse.error(400, "bad request", err.getMessage())));
				})
				.onErrorResume(Exception.class, err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage()))));
	}
	
	@DeleteMapping
	public Mono<ResponseEntity<ApiResponse>> removeAllMiddleMenByBankerId(@RequestParam("bankerId") String bankerId) {
		return this.middleManService.removeMiddleManByBankerId(bankerId)
				.collectList()
				.flatMap(middlemen -> {
					if (middlemen.isEmpty()) {
						return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
								.body(ApiResponse.error(404, "Middlemen not found.", "No middlemen available")));
					}
					return Mono.just(
							ResponseEntity.ok(ApiResponse.success("All middlemen have been deleted successfully.", 200, middlemen)));
				})
				.onErrorResume(MiddlemanNotFoundException.class, err -> 
					Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(ApiResponse.error(404, "Middlemen not found.", err.getMessage()))))
				.onErrorResume(Exception.class, err -> 
					Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ApiResponse.error(500, "An unexpected error occurred.", err.getMessage()))));
	}

}
