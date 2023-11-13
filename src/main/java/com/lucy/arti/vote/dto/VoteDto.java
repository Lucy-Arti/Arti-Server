package com.lucy.arti.vote.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class VoteDto {
    private Long id;

    public VoteDto(Long id) {
        this.id = id;
    }
}
