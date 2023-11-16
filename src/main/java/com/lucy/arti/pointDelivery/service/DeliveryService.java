package com.lucy.arti.pointDelivery.service;


import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.pointDelivery.domain.Delivery;
import com.lucy.arti.pointDelivery.dto.DeliveryDto;
import com.lucy.arti.pointDelivery.dto.DeliveryRequest;
import com.lucy.arti.pointDelivery.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private MemberRepository memberRepository;

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
            .build();
    }

//    public Delivery createDelivery(DeliveryRequest deliveryRequest, Member member) {
//        // Create the Delivery entity
//        Delivery delivery = Delivery.builder()
//                .name(deliveryRequest.getName())
//                .address(deliveryRequest.getAddress())
//                .phoneNumber(deliveryRequest.getPhoneNumber())
//                .delivery(deliveryRequest.isDelivery())
//                .member(member)
//                .build();
//
//        // Save the Delivery entity
//        return deliveryRepository.save(delivery);
//    }


    public Delivery createDelivery(DeliveryRequest deliveryRequest, Member member) {
        try {
            Delivery delivery = Delivery.builder()
                    .name(deliveryRequest.getName())
                    .address(deliveryRequest.getAddress())
                    .phoneNumber(deliveryRequest.getPhoneNumber())
                    .delivery(deliveryRequest.isDelivery())
                    .member(member)
                    .build();
            return deliveryRepository.save(delivery);
        } catch (Exception e) {
            // Log the exception and rethrow or handle it accordingly
            log.error("Error while creating delivery", e);
            throw new RuntimeException("Error while creating delivery", e);
        }
    }


}