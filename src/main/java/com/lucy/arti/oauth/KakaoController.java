package com.lucy.arti.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/kakao")
public class KakaoController {
    @Autowired
    private KakaoOauth2 kakaoOauth2;

    @PostMapping("/callback")
    public String kakaoCallback(@RequestBody AuthCode authCode) { // json 역직렬화 하는 방법 : @RequestBody에 매핑할 클래스를 따로 설정해야 한다.
        String authorizationCode = authCode.getAuthCode(); // json -> 객체로 받아온 변수
        System.out.println("authCode: " + authorizationCode);
        String accessToken = kakaoOauth2.getAccessToken(authorizationCode);
        System.out.println("accesstoken" + accessToken);

        return accessToken;
    }
}
