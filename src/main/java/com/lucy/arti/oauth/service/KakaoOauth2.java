package com.lucy.arti.oauth.service;

import com.lucy.arti.oauth.dto.KakaoProperties;
import com.lucy.arti.oauth.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOauth2 {
    private final KakaoProperties kakaoProperties;


    public KakaoUserInfo getUserInfo(String authorizedCode) {
        String accessToken = getAccessToken(authorizedCode);
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);
        return userInfo;
    }

    public String getAccessToken(String authorizedCode) {
        String clientId = kakaoProperties.getClient_id();
        String clientSecret = kakaoProperties.getClient_secret();
        String redirectUri = kakaoProperties.getRedirect_uri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("code", authorizedCode);
        params.add("client_secret", clientSecret);

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

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

    public KakaoUserInfo getUserInfoByToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

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
        System.out.println("사용자 정보: " + body);
        return new KakaoUserInfo(id, email, username, profile);
    }


}