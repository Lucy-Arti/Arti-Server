package com.lucy.arti.oauth.controller;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//public class OauthController {
//    @ResponseBody
//    @GetMapping("/api/oauth/kakao")
//    public void kakaoCallback(@RequestParam String code) {
//        System.out.println("code: " + code);
//    }
//}

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class OauthController {
    @GetMapping(value= "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}

