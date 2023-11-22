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
import org.springframework.security.access.annotation.Secured;
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


    @GetMapping("")
    @Secured({"ROLE_USER"})
    public List<DeliveryDto> getDeliveriesByMember() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).orElseThrow(() -> new RuntimeException("Member not found"));
        return deliveryService.getDeliveriesByMember(member);
    }

    @PostMapping("")
    @Secured({"ROLE_USER"})
    public ResponseEntity<String> createDelivery(@RequestBody DeliveryRequest deliveryRequest){
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).orElseThrow(() -> new RuntimeException("Member not found"));
        Delivery newDelivery = deliveryService.createDelivery(deliveryRequest, member);
        return new ResponseEntity<>("Delivery created successfully with ID: " + newDelivery.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDeliveryById(@PathVariable Long id) {
        DeliveryDto deliveryDto = deliveryService.getDeliveryById(id);
        if (deliveryDto != null) {
            return ResponseEntity.ok(deliveryDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
