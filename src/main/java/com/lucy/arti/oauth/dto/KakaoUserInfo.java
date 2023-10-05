package com.lucy.arti.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class KakaoUserInfo {
    Long id;
    String email;
    String username;
    String profile;
}
