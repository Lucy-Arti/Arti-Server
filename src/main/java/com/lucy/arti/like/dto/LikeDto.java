package com.lucy.arti.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeDto {
    private Long id;

    public LikeDto(Long id) {
        this.id = id;
    }

}
