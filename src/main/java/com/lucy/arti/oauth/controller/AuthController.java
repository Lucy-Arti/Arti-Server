package com.lucy.arti.oauth.controller;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.oauth.KakaoUserInfo;
import com.lucy.arti.oauth.service.AuthService;
import com.lucy.arti.oauth.dto.TokenDto;
import com.lucy.arti.oauth.dto.KakaoLoginRequestDto;
import com.lucy.arti.oauth.service.KakaoOauth2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakao")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Secured({"ROLE_USER"})
    public TokenDto kakaoLogin(@RequestBody KakaoLoginRequestDto kakaoLoginRequestDto) {
        return authService.createToken(authService.kakaoLogin(kakaoLoginRequestDto));
    }

    @PostMapping("/logout")
    @Secured({"ROLE_USER"})
    public ResponseEntity kakaoLogout(@RequestHeader(name = "Authorization") String bearerToken) {
        return authService.logout(bearerToken);
    }

    @GetMapping("/info")
    @Secured({"ROLE_USER"}) // Secured 추가
    public Member getUserInfoByToken(@RequestHeader(name = "Authorization") String accessToken) {
        return authService.getByAccessToken(accessToken);
    }
}

