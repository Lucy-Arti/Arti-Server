package com.lucy.arti.oauth.service;

import com.lucy.arti.oauth.KakaoProperties;
import com.lucy.arti.oauth.KakaoUserInfo;
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
        //인가코드 -> 엑세스 토큰
        log.info("KakaouOauth2.java/getUserInfo에 넘어옴" + authorizedCode);
        String accessToken = getAccessToken(authorizedCode);
        log.info("엑세스");
        //엑세스 토큰 -> 카카오 사용자 정보
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);
        return userInfo;
    }

    public String getAccessToken(String authorizedCode) {
//        log.info("getAccessToken에서" + authorizedCode);
        String clientId = kakaoProperties.getClient_id();
        String clientSecret = kakaoProperties.getClient_secret();
        String redirectUri = kakaoProperties.getRedirect_uri();

        // http header 생성
        // kakao developers 문서에 accessToken을 요청할 때 header의 content-type이 정해져있다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //http Params 설정 : POST인데 param으로 넣어야 한다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("code", authorizedCode);
        params.add("client_secret", clientSecret);
        log.info(redirectUri);

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
        log.info("엑세스 넘어옴" + accessToken);

        return accessToken;
    }

    public KakaoUserInfo getUserInfoByToken(String accessToken) {
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
        System.out.println("사용자 정보: " + body);
        return new KakaoUserInfo(id, email, username, profile);
    }


}