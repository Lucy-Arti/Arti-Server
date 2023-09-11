package com.lucy.arti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) // 로그인 페이지 생략
public class ArtiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtiApplication.class, args);
	}

}
