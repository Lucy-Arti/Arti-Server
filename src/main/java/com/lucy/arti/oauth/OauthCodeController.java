package com.lucy.arti.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OauthCodeController {
    @PostMapping("/member/kakao/authcode")
    public ResponseEntity<String> recieveAuthCode(@RequestBody String authCode) {
        return ResponseEntity.ok("인가코드" + authCode);
    }
}
