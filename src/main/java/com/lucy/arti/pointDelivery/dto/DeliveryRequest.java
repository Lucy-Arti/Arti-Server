package com.lucy.arti.pointDelivery.dto;

import com.lucy.arti.pointShop.domain.ShopItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Builder
public class DeliveryRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private boolean delivery;
    private Long itemId;
}
