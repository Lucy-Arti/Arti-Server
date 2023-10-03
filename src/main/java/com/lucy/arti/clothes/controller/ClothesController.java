package com.lucy.arti.clothes.controller;

import com.lucy.arti.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(clothesService.getAll());
    }

    @GetMapping("{clothesId}")
    public ResponseEntity<?> getById(@PathVariable Long clothesId) {
        return ResponseEntity.ok(clothesService.getById(clothesId));
    }
}
