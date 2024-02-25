package com.lucy.arti.clothes.controller;

import com.lucy.arti.clothes.domain.Type;
import com.lucy.arti.clothes.dto.ClothesCreateRequestDto;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.clothes.service.ClothesService;
import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("/api/v1/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(clothesService.getAll());
    }

    @PostMapping
    public ResponseEntity<String> create(@ModelAttribute ClothesCreateRequestDto requestDto,
        @RequestParam MultipartFile preview, @RequestParam MultipartFile img) throws IOException {

        String createdClothesId = clothesService.createClothes(requestDto, preview, img);
        URI location = URI.create("/api/v1/clothes" + createdClothesId);
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{clothesId}")
    public ResponseEntity<ClothesDetailResponseDto> update(
        @PathVariable Long clothesId,
        @ModelAttribute ClothesCreateRequestDto requestDto,
        MultipartFile preview, MultipartFile img) throws IOException {
        return ResponseEntity.ok(clothesService.update(clothesId, requestDto, preview, img));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getByType(@PathVariable String type, @RequestParam(value="page", defaultValue ="0") int page) {

        if (!Objects.equals(type, Type.sketch.toString()) && !Objects.equals(type,
            Type.product.toString())) {
            throw BusinessException.from(ErrorCode.CLOTHES_INVALID_TYPE);
        }

        Type requestType = Type.valueOf(type);
        return ResponseEntity.ok(clothesService.getTypeAll(requestType, page));
    }

    @GetMapping("/{clothesId}")
    public ResponseEntity<?> getById(@PathVariable Long clothesId) {
        return ResponseEntity.ok(clothesService.getById(clothesId));
    }

    // 옷 검색 api
    @GetMapping("/search")
    public ResponseEntity<List<?>> searchClothes(@RequestParam("query") String query) {
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
