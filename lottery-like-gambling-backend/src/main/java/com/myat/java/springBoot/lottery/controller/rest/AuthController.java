package com.myat.java.springBoot.lottery.controller.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myat.java.springBoot.lottery.dto.UserDto;
import com.myat.java.springBoot.lottery.model.Banker;
import com.myat.java.springBoot.lottery.model.JWTToken;
import com.myat.java.springBoot.lottery.model.MiddleMan;
import com.myat.java.springBoot.lottery.model.User;
import com.myat.java.springBoot.lottery.response.ApiResponse;
import com.myat.java.springBoot.lottery.service.AuthService;

import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.Validator;


@RestController
@RequestMapping("/v1/auth")
public class AuthController {
	
	@Autowired
	private Validator validation;
	
	@Autowired
	AuthService authService;
	
    @PostMapping("/register/user")
    public Mono<ResponseEntity<ApiResponse>> registerUser(@Valid @RequestBody User user) {
        if (!this.validation.validate(user).isEmpty()) {
            return Mono.error(new RuntimeException("Bad request"));
        }
        return this.authService.registerUser(user)
        		.flatMap(data ->
        				Mono.just(ResponseEntity.ok()
                                .body(ApiResponse.success("login success", 200, data.getToken())))
        				)
        		.onErrorResume(RuntimeException.class, err-> {
				return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "bad request", err.getMessage())));
			});
    }
    
//    @PostMapping("/register/banker")
//    public Mono<ResponseEntity<ApiResponse>> registerBanker(@Valid @RequestBody Banker banker) {
//        if (!this.validation.validate(banker).isEmpty()) {
//            return Mono.error(new RuntimeException("Bad request"));
//        }
//        return this.authService.registerBanker(banker)
//        		.flatMap(data ->
//        				Mono.just(ResponseEntity.ok()
//                                .body(ApiResponse.success("login success", 200, data.getToken())))
//        				)
//        		.onErrorResume(RuntimeException.class, err-> {
//				return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "bad request", err.getMessage())));
//			});
//    }
    
//    @PostMapping("/register/middleman")
//    public Mono<ResponseEntity<ApiResponse>> registerMiddleman(@Valid @RequestBody MiddleMan middleMan) {
//        if (!this.validation.validate(middleMan).isEmpty()) {
//            return Mono.error(new RuntimeException("Bad request"));
//        }
//        return this.authService.registerMiddleMan(middleMan)
//        		.flatMap(data ->
//        				Mono.just(ResponseEntity.ok()
//                                .body(ApiResponse.success("login success", 200, data.getToken())))
//        				)
//        		.onErrorResume(RuntimeException.class, err-> {
//				return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "bad request", err.getMessage())));
//			});
//    }
   
    @PostMapping("/login")
    public Mono<ResponseEntity<ApiResponse>> login(@RequestBody User user) {
        return authService.login(user)
            .flatMap(token -> {
                ResponseCookie jwtCookie = ResponseCookie.from("jwt", token.getToken()) // Store JWT in cookie
                        .httpOnly(false)
                        .secure(false) // Set to false for local testing if needed
                        .path("/")
                        .maxAge(86400) // 1 day expiration
                        .build();
                
                

                return Mono.just(ResponseEntity.ok()
                        .header("Set-Cookie", jwtCookie.toString())
                        .body(ApiResponse.success("login success", 200, token)));
            })
            .onErrorResume(BadCredentialsException.class,err -> {
				return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, "incorrect username or password", err.getMessage())));
			});
    }
    
    @PostMapping("/logout")
    public Mono<ResponseEntity<Map<String, String>>> logout() {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        Map<String, String> body = Map.of("message", "Logged out successfully");

        return Mono.just(ResponseEntity.ok()
                .header("Set-Cookie", jwtCookie.toString())
                .body(body));
    }

    
    @GetMapping("/me")
    public Mono<ResponseEntity<ApiResponse>> getCurrentUser(ServerHttpRequest request) {
        // Extract the "jwt" cookie
        String jwt = request.getCookies().getFirst("jwt") != null
            ? request.getCookies().getFirst("jwt").getValue()
            : null;

        // Validate the JWT value
        if (jwt == null || jwt.isEmpty()) {
            return Mono.just(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "User is not authenticated.", "JWT is missing or empty"))
            );
        }

        // Process the valid JWT
        return this.authService.getUserFromToken(jwt)
            .map(user -> ResponseEntity.ok().body(ApiResponse.success("User is authenticated", 200, user)))
            .onErrorResume(ex -> {
                // Handle JWT parsing/validation errors
                return Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error(401, "User is not authenticated.", ex.getMessage()))
                );
            });
    }


	
}
