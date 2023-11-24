package com.lucy.arti.point.repository;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.pointDelivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point,Long> {

    Point findByMember(Member member);
    Point findByCode(String code);



}
