package com.lucy.arti.vote.controller;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.vote.dto.VoteListResponseDto;
import com.lucy.arti.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @GetMapping("")
    public ResponseEntity<?> getVoteList() {
        return ResponseEntity.ok(new VoteListResponseDto(true, voteService.getVoteList().stream().map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList()));
    }

}
