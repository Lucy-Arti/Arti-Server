package com.lucy.arti.pointShop.dto;

import com.lucy.arti.pointShop.domain.ShopCategory;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class ShopDto {
    private Long id;
    private String title;
    private String brand;
    private String thumnail;
    private String image;
    private String detail;
    private Long price;
    private ShopCategory category;
    private boolean delivery;
}
