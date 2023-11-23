package com.lucy.arti.pointShop.controller;

import com.lucy.arti.pointShop.domain.ShopCategory;
import com.lucy.arti.pointShop.dto.ShopDto;
import com.lucy.arti.pointShop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/{category}")
    public List<ShopDto> getItemsByCategory(@PathVariable ShopCategory category){
        return shopService.getItemsByCategory(category);
    }

    //
    @GetMapping("/item/{id}")
    public ShopDto getItemById(@PathVariable Long id) {
        return shopService.getItemById(id);
    }

}
