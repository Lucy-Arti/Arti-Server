package com.lucy.arti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;

@SpringBootApplication
public class ArtiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ArtiApplication.class, args);
	}
}
