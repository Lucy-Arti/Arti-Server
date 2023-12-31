package com.lucy.arti.vote.controller;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.vote.dto.VoteListResponseDto;
import com.lucy.arti.vote.dto.VoteRequestDto;
import com.lucy.arti.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @GetMapping("")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> getVoteList(final Authentication authentication) {
        voteService.isPossibleVote(authentication);
        return ResponseEntity.ok(new VoteListResponseDto(true, voteService.getVoteList().stream().map(x -> ClothesDetailResponseDto.of(x, x.getDesigner())).toList()));
    }

    @GetMapping("/possible")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> isPossibleVotes(final Authentication authentication) {
        return ResponseEntity.ok(voteService.isPossibleVote(authentication));
    }
    @PostMapping("")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> votes(final Authentication authentication, @RequestBody VoteRequestDto voteRequestDto) {
        if (voteService.isPossibleVote(authentication)) {
            boolean success = voteService.vote(authentication, voteRequestDto);
            if (success) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}


