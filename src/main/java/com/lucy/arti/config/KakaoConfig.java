package com.lucy.arti.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
@PropertySource(value = {"application.yml"}, factory = YamlLoadFactory.class)
@Getter
@Setter
@ToString
public class KakaoConfig {

}
