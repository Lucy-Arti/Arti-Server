package com.lucy.arti.pointDelivery.controller;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.member.service.MemberService;
import com.lucy.arti.pointDelivery.domain.Delivery;
import com.lucy.arti.pointDelivery.dto.DeliveryDto;
import com.lucy.arti.pointDelivery.dto.DeliveryRequest;
import com.lucy.arti.pointDelivery.dto.DeliveryRequest;
import com.lucy.arti.pointDelivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/buy")
public class DeliveryController {


    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthenticationHelper authenticationHelper;


//    @GetMapping("")
//    public List<DeliveryDto> getAllDeliveries() {
//        return deliveryService.getAllDeliveries();
//    }

//    @GetMapping("")
//    public List<DeliveryDto> getDeliveriesByMember(final Authentication authentication) {
//        long userId = Long.parseLong(authentication.getName());
//        Member member = memberRepository.findByKakaoId(userId).get();
//        return deliveryService.getDeliveriesByMember(member);
//    }
//    @PostMapping("/create")
//    public ResponseEntity<String> createDelivery(
//            @RequestBody DeliveryRequest deliveryRequest,
//            Authentication authentication) {
//        try {
//            Delivery newDelivery = deliveryService.createDelivery(deliveryRequest,authentication);
//            return new ResponseEntity<>("Delivery created successfully with ID: " + newDelivery.getId(), HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("")
    public List<DeliveryDto> getDeliveriesByMember() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).orElseThrow(() -> new RuntimeException("Member not found"));
        return deliveryService.getDeliveriesByMember(member);
    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createDelivery(
//            @RequestBody DeliveryRequest deliveryRequest) {
//        try {
//            Authentication authentication = authenticationHelper.getAuthentication();
//            Delivery newDelivery = deliveryService.createDelivery(deliveryRequest, authentication);
//            return new ResponseEntity<>("Delivery created successfully with ID: " + newDelivery.getId(), HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/create")
    public ResponseEntity<String> createDelivery(@RequestBody DeliveryRequest deliveryRequest){
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).orElseThrow(() -> new RuntimeException("Member not found"));
        Delivery newDelivery = deliveryService.createDelivery(deliveryRequest, member);
        return new ResponseEntity<>("Delivery created successfully with ID: " + newDelivery.getId(), HttpStatus.CREATED);
    }
}
