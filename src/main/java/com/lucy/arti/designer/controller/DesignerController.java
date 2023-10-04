package com.lucy.arti.designer.controller;

import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.designer.dto.DesignerPostDto;
import com.lucy.arti.designer.service.DesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/designers")
public class DesignerController {
//    private final DesignerService designerService;
//    @PostMapping("/")
//    public Designer createDesigner(@RequestBody DesignerPostDto designerPostDto) {
//        return designerService.
//    }

}
