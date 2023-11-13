package com.lucy.arti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ArtiApplication {
	public static void main(String[] args) { SpringApplication.run(ArtiApplication.class, args);}
}
