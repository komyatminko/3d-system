package com.myat.java.springBoot.lottery.serviceImpl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.myat.java.springBoot.lottery.dao.BankerDao;
import com.myat.java.springBoot.lottery.dao.MiddleManDao;
import com.myat.java.springBoot.lottery.dao.UserDao;
import com.myat.java.springBoot.lottery.model.User;

import reactor.core.publisher.Mono;

@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Autowired
    UserDao userDao;
    
    @Autowired
    BankerDao bankerDao;
    
    @Autowired
    MiddleManDao middleManDao;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
    	return this.findUserByUsername(username)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new BadCredentialsException(String.format("User %s not found in database", username))))
                .doOnNext(data->{
                	System.out.println("User "+ data);
                })
                .map(this::createSpringSecurityUser);
    }
    
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getRoles()
        		.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
    
    private Mono<User> findUserByUsername(String username) {
    	Mono<User> user;
        user = userDao.findByUsername(username)
            .cast(User.class) // Ensure compatibility
            .switchIfEmpty(bankerDao.findByUsername(username).cast(User.class))
            .switchIfEmpty(middleManDao.findByUsername(username).cast(User.class))
            .doOnNext(data-> 
            	System.out.println("User after finding " + data)
            );
        
        return user;
    }
}

