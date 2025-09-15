package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		/*
		UserRepository userRepository = new UserRepository();
		UserBusiness UserBusiness = new UserBusiness(userRepository);
		UserController userController = new UserController(userBusiness);
		*/
	}
}
