package com.lucy.arti.pointShop.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.lucy.arti.exception.ErrorMessage;
import com.lucy.arti.exception.ErrorResult;
import com.lucy.arti.pointShop.domain.ShopCategory;
import com.lucy.arti.pointShop.domain.ShopItem;
import com.lucy.arti.pointShop.dto.ShopDto;
import com.lucy.arti.pointShop.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

//    public List<ShopDto> getItemsByCategory(ShopCategory category){
//        return shopRepository.findByCategory(category)
//                .stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//
//    }

    public List<ShopDto> getItemsByCategory(ShopCategory category) {
        List<ShopItem> items = shopRepository.findByCategory(category);
        if (items.isEmpty()) {
            throw new NotFoundException("No items found for category: " + category);
        }

        return items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

//    public ShopDto getItemById(Long id) {
//        return shopRepository.findById(id)
//                .map(this::convertToDto)
//                .orElse(null);
//    }


    public ShopDto getItemById(Long id) {
        return shopRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Item not found with ID: " + id));
    }


    private ShopDto convertToDto(ShopItem shopItem) {
        if (shopItem == null) {
            return null;
        }

        ShopDto shopDto = new ShopDto();
        shopDto.setId(shopItem.getId());
        shopDto.setTitle(shopItem.getTitle());
        shopDto.setBrand(shopItem.getBrand());
        shopDto.setThumnail(shopItem.getThumnail());
        shopDto.setImage(shopItem.getImage());
        shopDto.setDetail(shopItem.getDetail());
        shopDto.setPrice(shopItem.getPrice());
        shopDto.setCategory(shopItem.getCategory());
        shopDto.setDelivery(shopItem.isDelivery());

        return shopDto;
    }

}
