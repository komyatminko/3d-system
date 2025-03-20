package com.myat.java.springBoot.lottery.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.myat.java.springBoot.lottery.security.JWTReactiveAuthenticationManager;
import com.myat.java.springBoot.lottery.security.JwtAuthenticationFilter;
import com.myat.java.springBoot.lottery.security.JwtUtil;


@Configuration
@EnableWebFluxSecurity

public class SecurityConfig {

	@Autowired
	ReactiveUserDetailsService reactiveUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private static final String[] AUTH_WHITELIST = {
            "/public/**",
            "/v1/books/**",
    };
	
	
	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
	    return http
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        .csrf(ServerHttpSecurity.CsrfSpec::disable) 
	        .authorizeExchange(exchanges -> exchanges
	            .pathMatchers("/api/auth/register", "/api/auth/login", "/api/auth/me").permitAll()
//	            .pathMatchers(AUTH_WHITELIST).permitAll()
	            .anyExchange().authenticated()
	        )
	        .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION) 
	        .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION) 
	        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) 
	        .formLogin(ServerHttpSecurity.FormLoginSpec::disable) 
	        .build();
	}
	
	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
	    return new JwtAuthenticationFilter(jwtUtil);
	}
	
	@Bean
    JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager() {
        JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager = new JWTReactiveAuthenticationManager(this.reactiveUserDetailsService, passwordEncoder());
        return repositoryReactiveAuthenticationManager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(repositoryReactiveAuthenticationManager());
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance()); 
        return authenticationWebFilter;
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200")); 
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Set-Cookie", "Content-Type"));
        config.setAllowCredentials(true); // Allow credentials (cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
}
