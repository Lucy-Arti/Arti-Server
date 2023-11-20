package com.lucy.arti.like.controller;

import com.lucy.arti.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{clothesId}/member")
    @Secured({"ROLE_USER"})
    public boolean isLiked(final Authentication authentication, @PathVariable Long clothesId) {
        return clothesService.isLiked(authentication, clothesId);
    }
}
