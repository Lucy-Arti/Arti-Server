package com.lucy.arti.point.domain;

import com.lucy.arti.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Long point;
    private String code;
    private Long cotinue;
    private LocalDateTime lastCheck;
    private Long total;

    private String instagram;

    @Column(length = 2000)
    private String img;

    private Long invited;

    //mission
    private boolean comment; // 하루에 한번
    private boolean vote; // 하루에 한번
    private boolean visit; // 하루에 한번
    private boolean follow;
    private boolean story;// 하루에 한번
    private boolean friend = true; //늘 가능


    ////////////


    public Point(Member member) {
        this.member=member;
        this.code = generateCode();
        this.point=0L;
        this.cotinue=0L;
        this.total=0L;
        this.comment=true; // 하루에 한번
        this.vote=true; // 하루에 한번
        this.visit=true; // 하루에 한번
        this.follow=true;
        this.story=true; //하루에 한번
        this.invited=0L;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyFlags() {
        // 하루에 한 번 가능한 플래그들을 초기화
        this.comment = true;
        this.vote = true;
        this.visit = true;
        this.story = true;
    }

    @Scheduled(cron = "0 0 0 L * ?")
    public void resetMonthlyTotal() {
        // 매월 마지막 날에 total을 0으로 초기화
        this.total = 0L;
    }





    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 10;
    private static Set<String> existingCodes = new HashSet<>();
    public static String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        do {
            code.setLength(0);

            for (int i = 0; i < CODE_LENGTH; i++) {
                int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
                char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
                code.append(randomChar);
            }
        } while (isCodeDuplicated(code.toString()));

        existingCodes.add(code.toString());
        return code.toString();
    }

    private static boolean isCodeDuplicated(String newCode) {
        return existingCodes.contains(newCode);
    }

    ///////////

    public void addMember(Member member) {
        this.member = member;
    }

    public void addTotal(){this.total = this.total+1;}
    public void addPoint(Long score){this.point = this.point+score;}
    public void addContinue(){this.cotinue = this.cotinue+1;}
    public void addInvited(){this.invited = this.invited+1;}

    public void setLastCheck() {
        this.lastCheck = LocalDateTime.now();
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    public void setVisit(boolean visit) {
        this.visit = visit;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }


    public void setStory(boolean story) {
        this.story = story;
    }

    public void addInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void addImg(String img) {
        this.img = img;
    }
}
