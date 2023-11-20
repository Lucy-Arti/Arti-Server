package com.lucy.arti.winner.dto;

import lombok.Data;

@Data
public class WinnerDto {
    private Long id;

    public WinnerDto(Long id) {
        this.id = id;
    }
}