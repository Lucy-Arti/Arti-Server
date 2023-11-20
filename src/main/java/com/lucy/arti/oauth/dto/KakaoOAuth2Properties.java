package com.lucy.arti.oauth.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuth2Properties {
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;
}