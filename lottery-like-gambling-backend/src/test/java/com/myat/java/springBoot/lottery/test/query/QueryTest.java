package com.myat.java.springBoot.lottery.test.query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myat.java.springBoot.lottery.dao.BankerDao;
import com.myat.java.springBoot.lottery.dao.MiddleManDao;

@SpringBootTest
public class QueryTest {
	
	@Autowired
	MiddleManDao middlemanDao;

	@Autowired
	BankerDao bankerDao;
	
	@Test
	void retrieveMiddleman() {
		String name = "BANKER 1";
		this.bankerDao.findByUsername(name)
		.subscribe(data-> {
			System.out.println("data " + data);
		});
		
	}

	
}
