package com.lucy.arti.view;

import com.lucy.arti.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recent")
@Slf4j
public class ViewController {
    private final ViewService viewService;

    @GetMapping("/{clothesId}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> getByIdWithToken(final Authentication authentication, @PathVariable Long clothesId) {
        return ResponseEntity.ok(viewService.getByIdWithToken(authentication, clothesId));
    }

    @GetMapping
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> getRecent(final Authentication authentication) {
        return ResponseEntity.ok(viewService.getAllRecent(authentication));
    }

}
