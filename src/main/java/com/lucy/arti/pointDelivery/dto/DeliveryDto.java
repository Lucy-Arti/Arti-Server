package com.lucy.arti.pointDelivery.dto;

import com.lucy.arti.pointShop.domain.ShopItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DeliveryDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private boolean delivery;
    private ShopItem item;
    private String status;
    private LocalDateTime created_at;

}
