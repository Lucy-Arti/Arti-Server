package com.lucy.arti.designer.controller;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.service.ClothesService;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.designer.dto.DesignerPostDto;
import com.lucy.arti.designer.service.DesignerService;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/designers")
public class DesignerController {

    private final DesignerService designerService;

    @PostMapping
    public ResponseEntity<String> createDesigner(
        @ModelAttribute DesignerPostDto designerPostDto,
        @RequestParam(required = false) MultipartFile designerProfile) throws IOException {
        String createdDesignerId = designerService.create(designerPostDto, designerProfile);
        URI uri = URI.create("/api/v1/designers/" + createdDesignerId);
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{designerId}")
    public ResponseEntity<?> updateDesigner (@PathVariable Long designerId,
        @ModelAttribute DesignerPostDto postDto,
        MultipartFile designerProfile) throws IOException {
        return ResponseEntity.ok(designerService.update(designerId, postDto, designerProfile));
    }

    @GetMapping("/{designerId}")
    public ResponseEntity<?> getById(@PathVariable Long designerId) {
        return ResponseEntity.ok(designerService.getById(designerId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(designerService.getAll());
    }

}
