package com.lucy.arti.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VoteRequestDto {

    private List<Integer> fourth;

    private Integer second;

    private Integer first;
}
