package com.lucy.arti.pointHistory.repository;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.pointDelivery.domain.Delivery;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.dto.PointHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {
//    List<PointHistoryDto> findByPoint(Point point);
@Query("SELECT new com.lucy.arti.pointHistory.dto.PointHistoryDto(p.id, p.title, p.score, p.created_at) FROM PointHistory p WHERE p.point = :point")
List<PointHistoryDto> findByPoint(@Param("point") Point point);

}
