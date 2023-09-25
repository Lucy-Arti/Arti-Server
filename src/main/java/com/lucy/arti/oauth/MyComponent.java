package com.lucy.arti.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyComponent {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientSecret;

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public String getKakaoClientSecret() {
        return kakaoClientSecret;
    }
}
