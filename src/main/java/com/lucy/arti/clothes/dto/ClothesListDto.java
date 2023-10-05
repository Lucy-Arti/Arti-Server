package com.lucy.arti.clothes.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClothesListDto {
    private Long clothesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String detailImg;
    private Long likeCount;
    private String clothesName;
    private String preview;
    private Long designerId;
}
