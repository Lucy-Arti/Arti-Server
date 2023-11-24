package com.lucy.arti.pointDelivery.service;


import com.lucy.arti.member.domain.Member;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.point.repository.PointRepository;
import com.lucy.arti.pointDelivery.domain.Delivery;
import com.lucy.arti.pointDelivery.dto.DeliveryDto;
import com.lucy.arti.pointDelivery.dto.DeliveryRequest;
import com.lucy.arti.pointDelivery.dto.DeliveryUpdateDto;
import com.lucy.arti.pointDelivery.repository.DeliveryRepository;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.repository.PointHistoryRepository;
import com.lucy.arti.pointShop.domain.ShopItem;
import com.lucy.arti.pointShop.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    public List<DeliveryDto> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryDto> getDeliveriesByMember(Member member) {
        List<Delivery> deliveries = deliveryRepository.findByMember(member);
        return deliveries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DeliveryDto convertToDto(Delivery delivery) {
        return DeliveryDto.builder()
            .id(delivery.getId())
            .name(delivery.getName())
            .address(delivery.getAddress())
            .phoneNumber(delivery.getPhoneNumber())
            .delivery(delivery.isDelivery())
                .item(delivery.getItem())
                .created_at(delivery.getCreated_at().toLocalDate())
                .status(delivery.getStatus())
            .build();
    }


    public Delivery createDelivery(DeliveryRequest deliveryRequest, Member member) {
        ShopItem shopItem = shopRepository.findById(deliveryRequest.getItemId())
                .orElseThrow(() -> new RuntimeException("ShopItem not found"));
        Point point = pointRepository.findByMember(member);

        if(point.getPoint()<shopItem.getPrice()){
            throw new IllegalArgumentException("포인트가 부족하여 구매할 수 없습니다.");
        }
        else {
            try {
                Delivery delivery = Delivery.builder()
                        .name(deliveryRequest.getName())
                        .address(deliveryRequest.getAddress())
                        .phoneNumber(deliveryRequest.getPhoneNumber())
                        .delivery(deliveryRequest.isDelivery())
                        .member(member)
                        .item(shopItem)
                        .build();

                String title = shopItem.getTitle() + "구매";
                Long score = -(shopItem.getPrice());
                point.addPoint(score);
                pointRepository.save(point);
                PointHistory pointHistory = new PointHistory(point, title, score);
                pointHistoryRepository.save(pointHistory);
                return deliveryRepository.save(delivery);
            } catch (Exception e) {
                // Log the exception and rethrow or handle it accordingly
                log.error("Error while creating delivery", e);
                throw new RuntimeException("Error while creating delivery", e);
            }
        }
    }

    public DeliveryDto getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id).orElse(null);

        if (delivery != null) {
            return convertToDto(delivery);
        }

        return null;
    }

    @Transactional
    public DeliveryUpdateDto updateStatus(Long deliveryId, String status) {
        Delivery updateDelivery = deliveryRepository.findByIdOrThrow(deliveryId);
        updateDelivery.updateStatus(status);
        deliveryRepository.save(updateDelivery);
        return DeliveryUpdateDto.of(deliveryId, status);
    }

}
