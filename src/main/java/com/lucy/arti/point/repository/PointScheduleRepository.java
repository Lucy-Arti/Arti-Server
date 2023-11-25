package com.lucy.arti.point.repository;


import com.lucy.arti.point.domain.Point;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface PointScheduleRepository extends CrudRepository<Point, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Point p SET p.comment = true, p.vote = true, p.visit = true, p.commentCount = 0L")
    void updateDailyToTrue();

    @Transactional
    @Modifying
    @Query("UPDATE Point p SET p.story = true")
    void updateTueToTrue();

    @Transactional
    @Modifying
    @Query("UPDATE Point p SET p.total = 0")
    void updateMonthToTrue();
}