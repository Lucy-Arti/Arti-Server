package com.lucy.arti.point.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointCreateDto {

    private Long memberId;
    private Long point;
    private String title;

}
