package com.lucy.arti.point.service;

import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.point.dto.PointAdminDto;
import com.lucy.arti.point.dto.PointCreateDto;
import com.lucy.arti.point.dto.TotalPointDto;
import com.lucy.arti.point.repository.PointRepository;
import com.lucy.arti.pointDelivery.repository.DeliveryRepository;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.dto.PointHistoryDto;
import com.lucy.arti.pointHistory.repository.PointHistoryRepository;
import com.lucy.arti.pointHistory.service.PointHistoryService;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PointService {

    private static final int ZERO = 0;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;


    @Autowired
    private PointHistoryService pointHistoryService;

    @Autowired
    private MemberRepository memberRepository;

    public String getCurrentUserCode(Member member) {
        Point point = pointRepository.findByMember(member);

        if (point != null) {
            return point.getCode();
        } else {
            return "No code available";
        }
    }

    public TotalPointDto getMonthTotal(Member member) {
        Point point = pointRepository.findByMember(member);

        if (point != null) {
            return new TotalPointDto(point.getTotal(), point.getPoint());
        } else {
            throw new RuntimeException("Member not found");
        }
    }

    public Long getContinueCount(Member member) {
        Point point = pointRepository.findByMember(member);

        if (point != null) {
            return point.getCotinue();
        } else {
            throw new RuntimeException("Member not found");
        }
    }

    @Transactional
    public Long checkIn(Member member) {
        Point point = pointRepository.findByMember(member);

        if (point.isVisit()==false) {
            return 2L;
        }
        point.setVisit(false);
        point.addTotal();

        LocalDateTime lastCheck = point.getLastCheck();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);


        if (lastCheck != null && lastCheck.toLocalDate().equals(yesterday.toLocalDate())) {
            long hoursBetween = ChronoUnit.HOURS.between(lastCheck, now);

            if (Math.abs(hoursBetween) <= 24) {
                point.addContinue();
            } else {
                point.resetContinue();
                point.addContinue();
            }
            point.setLastCheck();
        } else {
            point.resetContinue();
            point.addContinue();
            point.setLastCheck();
        }

        // PointHistory 생성
        PointHistory pointHistory = new PointHistory(point, "출석체크", 30L);
        pointHistoryRepository.save(pointHistory);
        point.addPoint(30L);
        point.setLastCheck();

        // Point 저장
        pointRepository.save(point);
        return 1L;
    }

    public void autoCheck(Member member){
        Point point = pointRepository.findByMember(member);
        Long cotinueValue = point.getCotinue();

        if (cotinueValue == 3 || cotinueValue == 5 || cotinueValue == 7) {
            PointHistory pointHistory = new PointHistory(point, "3/5/7 연속 출석 보상", 50L);
            pointHistoryRepository.save(pointHistory);
            point.addPoint(50L);
            pointRepository.save(point);
        }
    }


    @Autowired
    private S3Upload s3Upload;

    @Transactional
    public Long uploadImage(MultipartFile image, Member member) throws IOException {
        Point searchpoint = pointRepository.findByMember(member);
        if (searchpoint == null) {
            searchpoint = new Point(); // 기존 Point가 없으면 새로운 Point 객체 생성
            searchpoint.addMember(member);
        }

        if (!image.isEmpty()) {
            String storedFileName = s3Upload.upload(image);
            searchpoint.addImg(storedFileName);
        }else{
        }
        searchpoint.setStory(false);
        searchpoint.setImg_time();
        Point savedPoint = pointRepository.save(searchpoint);
        return savedPoint.getId();
    }

    public void updateInstagram(Point point, String instagram){
        point.addInstagram(instagram);
        point.setFollow(false);
        pointRepository.save(point);
    }

    public void plusPoint(Point point){
        point.addPoint(50L);
        pointRepository.save(point);

        // PointHistory 생성
        PointHistory pointHistory = new PointHistory(point, "출석체크 추가 포인트", 50L);
        pointHistoryRepository.save(pointHistory);
    }

    public ResponseEntity<?> getUserPointsAndHistory(Point point){
        Long savedpoint = point.getPoint();
        List<PointHistoryDto> findhistories = pointHistoryRepository.findByPoint(point);
        Map<String, Object> response = new HashMap<>();
        response.put("savedPoint", savedpoint);
        response.put("pointHistory", findhistories);
        return ResponseEntity.ok(response);
    }

    public void redeemCode(String inputCode, Point point) {
        Point codePoint = pointRepository.findByCode(inputCode);
        if (codePoint == null) {
            throw new EntityNotFoundException("invalid code");
        }

        codePoint.addPoint(1000L);
        codePoint.addInvited();
        pointRepository.save(codePoint);

        point.addPoint(1500L);
        point.setCodeUse(true);
        pointRepository.save(point);
    }

    public Long calculateReward(Long commentCount) {
        if (commentCount >= 5) {
            return 0L;
        } else if (commentCount == 4) {
            return 50L;
        } else if (commentCount == 3) {
            return 100L;
        } else if (commentCount == 2) {
            return 150L;
        } else if (commentCount == 1) {
            return 200L;
        } else if(commentCount == 0){
            return 250L;
        }else{
            throw new IllegalArgumentException("Comment count cannot be null");
        }
    }

    public List<PointAdminDto> getPointInfo() {
        return pointRepository.findAll().stream()
            .map(single -> PointAdminDto.of(
                single.getMember().getId(),
                single.getPoint(),
                getGivenPoint(single),
                getUsedPoint(single)
            ))
            .collect(Collectors.toList());
    }

    private Long getGivenPoint(Point point) {
        return point.getPointHistories().stream()
            .filter(history -> history.getScore() > ZERO)
            .mapToLong(PointHistory::getScore)
            .sum();
    }

    private Long getUsedPoint(Point point) {
        return point.getPointHistories().stream()
            .filter(history -> history.getScore() < ZERO)
            .mapToLong(PointHistory::getScore)
            .sum();
    }

    @Transactional
    public String uploadPoint(PointCreateDto request) {
        Member member = memberRepository.findByIdOrThrow(request.getMemberId());
        Point point = pointRepository.findByMember(member);
        point.addPoint(request.getPoint());
        pointRepository.save(point);

        PointHistory newHistory = new PointHistory(point, request.getTitle(),
            request.getPoint()
        );
        pointHistoryRepository.save(newHistory);
        return newHistory.getId().toString();
    }

}
