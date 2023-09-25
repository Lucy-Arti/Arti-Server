package com.lucy.arti.oauth;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.json.*;

@Component
public class KakaoOauth2 {

    public KakaoUserInfo getUserInfo(String authorizedCode) {
        //인가코드 -> 엑세스 토큰
        String accessToken = getAccessToken(authorizedCode);
        //엑세스 토큰 -> 카카오 사용자 정보
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);
        return userInfo;
    }

    public String getAccessToken(String authorizedCode) {
        MyComponent myComponent = new MyComponent();

        // http header 생성
        // kakao developers 문서에 accessToken을 요청할 때 header의 content-type이 정해져있다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //http body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "209c9251e18aa84300b9f4dc8047c6cd");
        params.add("redirect_uri", "https://lucy-arti.netlify.app/kakaologin");
        params.add("code", authorizedCode);
        params.add("client_secret", myComponent.getKakaoClientSecret());

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //http 요청
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String tokenJson = response.getBody();
        org.json.JSONObject rjson = new JSONObject(tokenJson);
        String accessToken = rjson.getString("access_token");

        return accessToken;
    }

    private KakaoUserInfo getUserInfoByToken(String accessToken) {
        //HttpHeader 오브젝트 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //httpheader와 httpbody 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        //http 요청
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        Long id = body.getLong("id");
        String email = body.getJSONObject("kakao_account").getString("email");
        String username = body.getJSONObject("properties").getString("nickname");
        String profile = body.getJSONObject("properties").getString("profile_image");
        System.out.println(body);
        return new KakaoUserInfo(id, email, username, profile);
    }


}