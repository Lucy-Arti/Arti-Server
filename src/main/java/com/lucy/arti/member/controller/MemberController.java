package com.lucy.arti.member.controller;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.dto.MemberUpdateResponseDto;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        List<Member> List = new ArrayList<>();
        List = memberRepository.findAll();
        return new ResponseEntity<>(List, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        Member savedMember = memberRepository.save(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }

    @GetMapping("/1st")
    public ResponseEntity<?> getWinningClothes(final Authentication authentication) {
        return ResponseEntity.ok(memberService.victory(authentication));
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getSavedClothes(final Authentication authentication) {
        return ResponseEntity.ok(memberService.saveListShow(authentication));
    }

    @PostMapping("/profile/name")
    public ResponseEntity<MemberUpdateResponseDto> updateName(final Authentication authentication, @RequestParam(name = "customName") String customName) {
        return ResponseEntity.ok(memberService.updateNickname(authentication, customName));
    }
}