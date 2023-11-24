package com.lucy.arti.pointDelivery.dto;

import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.pointDelivery.domain.Delivery;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryUpdateDto {

    private Long deliveryId;
    private String status;

    public static DeliveryUpdateDto of(Long deliveryId, String status) {
        return DeliveryUpdateDto.builder()
            .deliveryId(deliveryId)
            .status(status)
            .build();
    }

}