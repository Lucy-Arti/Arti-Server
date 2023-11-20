package com.lucy.arti.point.service;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.point.repository.PointRepository;
import com.lucy.arti.pointDelivery.repository.DeliveryRepository;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.dto.PointHistoryDto;
import com.lucy.arti.pointHistory.repository.PointHistoryRepository;
import com.lucy.arti.pointHistory.service.PointHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;


    @Autowired
    private PointHistoryService pointHistoryService;

    public String getCurrentUserCode(Member member) {
        Point point = pointRepository.findByMember(member);

        if (point != null) {
            return point.getCode();
        } else {
            return "No code available";
        }
    }

    public Long getMonthTotal(Member member) {
        Point point = pointRepository.findByMember(member);

        if (point != null) {
            return point.getTotal();
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
        log.info(String.valueOf(point.getLastCheck()));
        log.info(String.valueOf(point.getCotinue()));
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

    public Long uploadImage(MultipartFile image, Member member) throws IOException {
        Point searchpoint = pointRepository.findByMember(member);
        log.info("service 넘어옴");
        if (searchpoint == null) {
            searchpoint = new Point(); // 기존 Point가 없으면 새로운 Point 객체 생성
            searchpoint.addMember(member);
            log.info("새로운 포인트 생성");
        }

        if (!image.isEmpty()) {
            log.info("파일 안비어있음");
            String storedFileName = s3Uploader.upload(image, "image");
            log.info("파일 올려서 주소 ㅂ받아옴");
            log.info(storedFileName);
            searchpoint.addImg(storedFileName);
        }else{
            log.info("파일이 비어있");
        }
        searchpoint.setStory(false);
        Point savedPoint = pointRepository.save(searchpoint);
        log.info("point저장하고 이제 나감");
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
        log.info(String.valueOf(savedpoint));
        List<PointHistoryDto> findhistories = pointHistoryRepository.findByPoint(point);
        log.info(findhistories.toString());
        Map<String, Object> response = new HashMap<>();
        response.put("savedpoint", savedpoint);
        response.put("point history", findhistories);
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
}