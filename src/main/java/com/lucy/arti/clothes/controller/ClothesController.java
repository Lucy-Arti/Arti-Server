package com.lucy.arti.clothes.controller;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.clothes.repository.ClothesRepositoryCustom;
import com.lucy.arti.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    // 옷 검색 api
    @GetMapping("/search")
    public ResponseEntity<List<?>> searchClothes(@RequestParam("query") String query){
        return ResponseEntity.ok(clothesService.searchClothes(query));
    }
    @GetMapping("/designer/{designerId}")
    public ResponseEntity<?> getClothesById(@PathVariable Long designerId) {
        return ResponseEntity.ok(clothesService.getClothesByDesignerId(designerId));
    }
    @GetMapping("/score")
    public ResponseEntity<?> getClothesBySort() {
        return ResponseEntity.ok(clothesService.sortClothes());
    }

}
