package com.lucy.arti.designer.controller;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.service.ClothesService;
import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.designer.dto.DesignerPostDto;
import com.lucy.arti.designer.service.DesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/designers")
public class DesignerController {
    private final ClothesService clothesService;
//    @GetMapping("/{clotehsId}")
//    @Secured({"ROLE_USER"})
//    public List<Clothes> getClothesById(final Authentication authentication, @PathVariable Long clothesId) {
//        return clothesService.getClothesByDesignerId(authentication, clothesId);
//    }
}
