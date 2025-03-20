package com.myat.java.springBoot.lottery.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myat.java.springBoot.lottery.dao.UserDao;
import com.myat.java.springBoot.lottery.dto.UserDto;
import com.myat.java.springBoot.lottery.model.JWTToken;
import com.myat.java.springBoot.lottery.model.User;
import com.myat.java.springBoot.lottery.security.JWTReactiveAuthenticationManager;
import com.myat.java.springBoot.lottery.security.JwtUtil;
import com.myat.java.springBoot.lottery.service.AuthService;

import jakarta.validation.Validator;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService{
	
	private final JwtUtil tokenProvider;
    private final JWTReactiveAuthenticationManager authenticationManager;
    ModelMapper modelMapper = new ModelMapper();
    
    @Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtUtil tokenProvider,
            JWTReactiveAuthenticationManager authenticationManager,
            Validator validation) {
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Mono<JWTToken> register(User user) {
		return this.userDao.findByUsername(user.getUsername())
			    .map(userEntity->{
					throw new RuntimeException("User already exist");
				})
			    .switchIfEmpty(Mono.defer(()->this.registerUser(user)))
			    .cast(User.class)
			    .flatMap(saveUser->{
			    	System.out.println("New user have been saved "+saveUser);
			    	return this.login(user);
			    });
	}

	@Override
	public Mono<JWTToken> login(User user) {
		Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Mono<Authentication> authentication = this.authenticationManager.authenticate(authenticationToken);
        authentication.doOnError(throwable -> {
            throw new BadCredentialsException("Bad crendentials");
        });
        ReactiveSecurityContextHolder.withAuthentication(authenticationToken);

        return authentication.map(auth -> {
            String jwt = tokenProvider.createToken(auth);
            
            return new JWTToken(jwt);
        });
	}
	

	 Mono<User> registerUser(User user)
	    {
		   	User newUser = new User();
	    	newUser.setUsername(user.getUsername());
	    	newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
	    	newUser.setRole(user.getRole());
	    	newUser.setPhone(user.getPhone());
	    	newUser.setAddress(user.getAddress());
	    	return this.userDao.save(newUser);
	    }
	 
	 public Mono<UserDto> getUserFromToken(String token) {
	        return Mono.justOrEmpty(tokenProvider.validateToken(token))
	        	.map(isTokenValid-> tokenProvider.getUsernameFromToken(token))
	            .flatMap(username -> userDao.findByUsername(username))
	            .map(user-> this.modelMapper.map(user, UserDto.class));
	    }
}
