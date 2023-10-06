package com.lucy.arti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing // createdAt, updatedAt 자동 관리
@SpringBootApplication(exclude = {
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
})
public class ArtiApplication {
	// DateTime 시간 한국으로 설정
	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {
		SpringApplication.run(ArtiApplication.class, args);
	}

}
