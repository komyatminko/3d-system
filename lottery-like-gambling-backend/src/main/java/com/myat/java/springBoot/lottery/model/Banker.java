package com.myat.java.springBoot.lottery.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bankers")
public class Banker extends User{

}
