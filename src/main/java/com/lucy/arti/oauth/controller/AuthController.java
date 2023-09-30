package com.lucy.arti.oauth.controller;

import com.lucy.arti.member.model.MemberRepository;
import com.lucy.arti.oauth.service.AuthService;
import com.lucy.arti.oauth.dto.TokenDto;
import com.lucy.arti.oauth.dto.KakaoLoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;

    @PostMapping("/kakao/login")
    public TokenDto kakaoLogin(@RequestBody KakaoLoginRequestDto kakaoLoginRequestDto) {
        return authService.createToken(authService.kakaoLogin(kakaoLoginRequestDto));
    }

    @PostMapping("/kakao/logout")
    public ResponseEntity kakaoLogout(@RequestHeader(name = "Authorization") String bearerToken) {
        System.out.println("bearerToken = " + bearerToken);
        return authService.logout(bearerToken);
    }

    @GetMapping("")
    public Map getByAccessToken(@RequestHeader(name = "Authorization") String bearerToken) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", memberRepository.findByAccessToken(bearerToken).get().getId());
        return map;
    }
}

