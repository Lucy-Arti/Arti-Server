package com.lucy.arti.like.controller;

import com.lucy.arti.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final ClothesService clothesService;
    @PostMapping("/{clothesId}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> like(final Authentication authentication, @PathVariable Long clothesId) {
        clothesService.like(authentication, clothesId);
        return ResponseEntity.ok().build();
    }
}
