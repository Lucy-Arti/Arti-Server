package com.lucy.arti.point.controller;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.point.dto.InstagramRequest;
import com.lucy.arti.point.dto.PointDto;
import com.lucy.arti.point.dto.PointInfoDto;
import com.lucy.arti.point.dto.PointValuesResponse;
import com.lucy.arti.point.repository.PointRepository;
import com.lucy.arti.point.service.PointService;
import com.lucy.arti.pointDelivery.controller.AuthenticationHelper;
import com.lucy.arti.pointDelivery.service.DeliveryService;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.dto.PointHistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.lucy.arti.point.service.S3Uploader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Autowired
    private final S3Uploader s3Uploader;

    @GetMapping("")
    public PointInfoDto getPointInfo() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return new PointInfoDto(member.getPoint());
    }

    @GetMapping("/code")
    public ResponseEntity<String> getUserCode() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String userCode = pointService.getCurrentUserCode(member);
        return ResponseEntity.ok(userCode);
    }

    @GetMapping("/check")
    public ResponseEntity<Long> getMonthCheck() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Long MonthCheck = pointService.getMonthTotal(member);
        return ResponseEntity.ok(MonthCheck);
    }

    @GetMapping("/check/continue")
    public ResponseEntity<Long> getContinueCheck() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Long ContinueCheck = pointService.getContinueCount(member);
        return ResponseEntity.ok(ContinueCheck);
    }
    @PostMapping("/check")
    public ResponseEntity<String> checkIn() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Long result = pointService.checkIn(member);
        if (result == 2L) {
            return ResponseEntity.internalServerError().body("이미 출석했습니다");
        } else if (result == 1L) {
            return ResponseEntity.ok("출석 완료");
        }
        else{
            return (ResponseEntity<String>) ResponseEntity.internalServerError();
        }
    }

    @ResponseBody
    @PostMapping(value="/capture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long saveDiary(HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
        log.info("컨트롤러");
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());

        Member member = memberRepository.findByKakaoId(userId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));
        log.info(String.valueOf(image));
        log.info(String.valueOf(member));
        Long uploadCheck = pointService.uploadImage(image, member);
            log.info("Upload Check: {}", uploadCheck);
        return uploadCheck;
    }

    @ResponseBody
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
    public ResponseEntity<?> getHistoryAndPoint() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);
        return pointService.getUserPointsAndHistory(point);
    }

    @GetMapping("/invite")
    public ResponseEntity<PointValuesResponse> getInvitedValues() {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);
        if (point != null) {
            Long invitedTimes1000 = point.getInvited() * 1000L;
            PointValuesResponse response = new PointValuesResponse(point.getInvited(), invitedTimes1000);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.info("point == null");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/invited")
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
    public ResponseEntity<Boolean> checkInvited(){
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Point point = pointRepository.findByMember(member);
//        Boolean result = pointService.getCodeUse(point);
        Boolean result = point.isCodeUse();
        return new ResponseEntity<>(result,HttpStatus.OK);

    }

}
