package com.lucy.arti.pointDelivery.repository;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.pointDelivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

    List<Delivery> findByMember(Member member);
    List<Delivery> findAll();
}