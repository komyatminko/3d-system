package com.myat.java.springBoot.lottery.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myat.java.springBoot.lottery.dao.BankerDao;
import com.myat.java.springBoot.lottery.dao.MiddleManDao;
import com.myat.java.springBoot.lottery.dao.UserDao;
import com.myat.java.springBoot.lottery.dto.BankerDto;
import com.myat.java.springBoot.lottery.dto.MiddleManDto;
import com.myat.java.springBoot.lottery.dto.UserDto;
import com.myat.java.springBoot.lottery.model.Banker;
import com.myat.java.springBoot.lottery.model.JWTToken;
import com.myat.java.springBoot.lottery.model.MiddleMan;
import com.myat.java.springBoot.lottery.model.Role;
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
    private BankerDao bankerDao;
    
    @Autowired
    private MiddleManDao middleManDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtUtil tokenProvider,
            JWTReactiveAuthenticationManager authenticationManager,
            Validator validation) {
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
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

	@Override
	public Mono<JWTToken> registerUser(User user) {
		
		return this.userDao.findByUsername(user.getUsername())
				.map(userEntity->{
					throw new RuntimeException("User already exist");
				})
				.switchIfEmpty(Mono.defer(()->this.saveUser(user)))
				.cast(User.class)
			    .flatMap(savedUser ->  this.login(savedUser));
	}

	@Override
	public Mono<BankerDto> registerBanker(Banker banker) {
		return this.bankerDao.findByUsername(banker.getUsername())
				.map(bankerEntity->{
					throw new RuntimeException("Banker already exist");
				})
				.switchIfEmpty(Mono.defer(()->this.saveBanker(banker)))
				.flatMap(bankerEntity -> Mono.just(this.modelMapper.map(bankerEntity, BankerDto.class)));
	}

	@Override
	public Mono<MiddleManDto> registerMiddleMan(MiddleMan middleman) {
		return this.middleManDao.findByUsername(middleman.getUsername())
				.map(bankerEntity->{
					throw new RuntimeException("Middleman already exist");
				})
				.switchIfEmpty(Mono.defer(()->this.saveMiddleman(middleman)))
				.cast(MiddleMan.class)
				.flatMap(middlemanEntity -> Mono.just(this.modelMapper.map(middlemanEntity, MiddleManDto.class)));
	}
	
	private Mono<User> saveUser(User user) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
		newUser.setRoles(user.getRoles());
		newUser.setPhone(user.getPhone());
		newUser.setAddress(user.getAddress());
		return this.userDao.save(newUser);
	}

	private Mono<Banker> saveBanker(Banker banker) {
		Banker newBanker = new Banker();
		newBanker.setUsername(banker.getUsername());
		newBanker.setPassword(this.passwordEncoder.encode(banker.getPassword()));
		newBanker.setRoles(banker.getRoles());
		newBanker.setPhone(banker.getPhone());
		newBanker.setAddress(banker.getAddress());
		newBanker.setAccountExpiredDate(banker.getAccountExpiredDate());
		newBanker.setIsAccountExpired(banker.getIsAccountExpired());
		return bankerDao.save(newBanker);
	}

	private Mono<MiddleMan> saveMiddleman(MiddleMan middleman) {
		return bankerDao.findById(middleman.getBankerId())
				.switchIfEmpty(Mono.error(new RuntimeException("Banker not found")))
				.flatMap(banker -> {
					middleman.setPassword(this.passwordEncoder.encode(middleman.getPassword()));
					return middleManDao.save(middleman);
				});
	}
		
	public Mono<UserDto> getUserFromToken(String token) {
		return Mono.justOrEmpty(tokenProvider.validateToken(token))
				.map(isTokenValid -> tokenProvider.getUsernameFromToken(token))
				.flatMap(username -> {
					return userDao.findByUsername(username)
							.switchIfEmpty(bankerDao.findByUsername(username))
							.switchIfEmpty(middleManDao.findByUsername(username));
				})
				.map(user -> this.modelMapper.map(user, UserDto.class));
	}



	

}
