package com.lucy.arti.pointDelivery.dto;

import com.lucy.arti.pointShop.domain.ShopItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DeliveryDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private boolean delivery;
    private ShopItem item;
    private String status;
    private LocalDate created_at;

    public DeliveryDto(Long id, String name, String address, String phoneNumber,
                       boolean delivery, ShopItem item, String status, LocalDateTime created_at) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.delivery = delivery;
        this.item = item;
        this.status = status;
        this.created_at = created_at.toLocalDate(); // LocalDateTime을 LocalDate로 변환
    }


}
