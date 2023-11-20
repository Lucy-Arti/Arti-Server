package com.lucy.arti.pointHistory.domain;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.point.domain.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "point_id")  // 외래 키로 참조되는 엔티티의 키를 지정
    private Point point;

    private String title;
    private Long score;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    public PointHistory(Point point, String title, Long score) {
        this.point = point;
        this.title = title;
        this.score = score;
        this.created_at=LocalDateTime.now();
    }
}
