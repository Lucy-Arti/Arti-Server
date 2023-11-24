package com.lucy.arti.point.controller;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.point.dto.*;
import com.lucy.arti.point.repository.PointRepository;
import com.lucy.arti.point.service.PointService;
import com.lucy.arti.point.service.S3Upload;
import com.lucy.arti.pointDelivery.controller.AuthenticationHelper;
import com.lucy.arti.pointDelivery.service.DeliveryService;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.dto.PointHistoryDto;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v2/point")
@RequiredArgsConstructor
public class PointController {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Autowired
    private PointService pointService;


    @GetMapping("")
    @Secured({"ROLE_USER"})
    public PointInfoDto getPointInfo() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        return new PointInfoDto(member.getPoint());
    }

    @GetMapping("/code")
    @Secured({"ROLE_USER"})
    public ResponseEntity<String> getUserCode() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        String userCode = pointService.getCurrentUserCode(member);
        return ResponseEntity.ok(userCode);
    }

    @GetMapping("/check")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> getMonthCheck() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        TotalPointDto MonthCheck = pointService.getMonthTotal(member);
        return ResponseEntity.ok(MonthCheck);
    }

    @GetMapping("/check/continue")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Long> getContinueCheck() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        Long ContinueCheck = pointService.getContinueCount(member);
        return ResponseEntity.ok(ContinueCheck);
    }

    @PostMapping("/check")
    @Secured({"ROLE_USER"})
    public ResponseEntity<String> checkIn() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        Long result = pointService.checkIn(member);
        if (result == 2L) {
            return ResponseEntity.internalServerError().body("이미 출석했습니다");
        } else if (result == 1L) {
            pointService.autoCheck(member);
            return ResponseEntity.ok("출석 완료");
        } else {
            return (ResponseEntity<String>) ResponseEntity.internalServerError();
        }
    }

    @ResponseBody
    @Secured({"ROLE_USER"})
    @PostMapping(value = "/capture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveDiary(HttpServletRequest request,
        @RequestParam(value = "image") MultipartFile image) throws IOException {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());

        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        Long uploadCheck = pointService.uploadImage(image, member);
        log.info("Upload Check: {}", uploadCheck);
        return ResponseEntity.ok(uploadCheck);
    }

    @ResponseBody
    @Secured({"ROLE_USER"})
    @PostMapping("/follow")
    public ResponseEntity<String> updateInstagram(@RequestBody InstagramRequest request) {
        try {
            Authentication authentication = authenticationHelper.getAuthentication();
            long userId = Long.parseLong(authentication.getName());
            Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
            Point point = pointRepository.findByMember(member);
            pointService.updateInstagram(point, request.getInstagram());
            return ResponseEntity.ok("Instagram updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update Instagram");
        }
    }

    @ResponseBody
    @Secured({"ROLE_USER"})
    @PostMapping("/check/plus")
    public ResponseEntity<String> plusCheck() {
        try {
            Authentication authentication = authenticationHelper.getAuthentication();
            long userId = Long.parseLong(authentication.getName());
            Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
            Point point = pointRepository.findByMember(member);
            pointService.plusPoint(point);
            return ResponseEntity.ok("point added successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add point");
        }
    }

    @GetMapping("/history")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> getHistoryAndPoint() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);
        return pointService.getUserPointsAndHistory(point);
    }

    @GetMapping("/invite")
    @Secured({"ROLE_USER"})
    public ResponseEntity<PointValuesResponse> getInvitedValues() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);
        if (point != null) {
            Long invitedTimes1000 = point.getInvited() * 1000L;
            PointValuesResponse response = new PointValuesResponse(point.getInvited(),
                invitedTimes1000);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/invited")
    @Secured({"ROLE_USER"})
    public ResponseEntity<String> invitedCode(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);

        pointService.redeemCode(code, point);

        return new ResponseEntity<>("포인트가 성공적으로 추가되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/invited")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Boolean> checkInvited() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);
//        Boolean result = pointService.getCodeUse(point);
        Boolean result = point.isCodeUse();
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/commentReward")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Long> getCommentReward() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);
        Long commentCount = point.getCommentCount();
        Long reward = pointService.calculateReward(commentCount);
        return ResponseEntity.ok(reward);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getAdminInfo() {
        return ResponseEntity.ok(pointService.getPointInfo());
    }

    @PostMapping("/admin")
    public ResponseEntity<?> uploadPoint (@ModelAttribute PointCreateDto request) {
        String pointId = pointService.uploadPoint(request);
        URI uri = URI.create("/api/v2/point/" + pointId);
        return ResponseEntity.created(uri).build();
    }

    @ResponseBody
    @Secured({"ROLE_USER"})
    @PostMapping(value="/capture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveDiary(HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());

        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Long uploadCheck = pointService.uploadImage(image, member);
        log.info("Upload Check: {}", uploadCheck);
        return ResponseEntity.ok(uploadCheck);
    }

}
