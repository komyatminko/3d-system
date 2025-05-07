package com.myat.java.springBoot.lottery.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class JWTReactiveAuthenticationManager implements ReactiveAuthenticationManager{

	private final PasswordEncoder passwordEncoder;
	
	private final ReactiveUserDetailsService userDetailsService;
	
	public JWTReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
//		System.out.println("password " + authentication.getCredentials());
		if(authentication.isAuthenticated()) {
            return Mono.just(authentication);
        }
		
        return Mono.just(authentication)
                .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                .cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(this::authenticateToken)
                .publishOn(Schedulers.parallel())
                .onErrorResume(e -> raiseBadCredentials1())
                .doOnNext(u-> System.out.println("password " + u.getPassword() + " authentication password " + authentication.getCredentials()))
                .filter(u -> passwordEncoder.matches((String) authentication.getCredentials(), u.getPassword()))
                .switchIfEmpty(Mono.defer(this::raiseBadCredentials2))
                .map(u -> new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), u.getAuthorities()));
	}
	
	private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(new BadCredentialsException("Invalid Credentials"));
    }
	private <T> Mono<T> raiseBadCredentials1() {
        return Mono.error(new BadCredentialsException("Invalid Credentials1"));
    }
	private <T> Mono<T> raiseBadCredentials2() {
        return Mono.error(new BadCredentialsException("Invalid Credentials2"));
    }
	
	private Mono<UserDetails> authenticateToken(final UsernamePasswordAuthenticationToken authenticationToken) {
        String username = authenticationToken.getName();
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            return this.userDetailsService.findByUsername(username);
        }

        return null;
    }

}
