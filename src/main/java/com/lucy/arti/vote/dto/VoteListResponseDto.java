package com.lucy.arti.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VoteListResponseDto {

    private boolean success;

    @JsonProperty("data")
    private List<ClothesDetailResponseDto> clothes;

}
