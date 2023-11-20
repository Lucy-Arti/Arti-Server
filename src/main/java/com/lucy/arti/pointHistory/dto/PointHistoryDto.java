package com.lucy.arti.pointHistory.dto;

import com.lucy.arti.point.domain.Point;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
public class PointHistoryDto {

    private Long id;
    private String title;
    private Long score;
    private LocalDateTime created_at;

    public PointHistoryDto(Long id, String title, Long score, LocalDateTime created_at) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.created_at = created_at;
    }

}
