package com.lucy.arti.vote.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.point.domain.Point;
import com.lucy.arti.point.repository.PointRepository;
import com.lucy.arti.pointHistory.domain.PointHistory;
import com.lucy.arti.pointHistory.repository.PointHistoryRepository;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.vote.dto.VoteRequestDto;
import com.lucy.arti.vote.repository.VoteRepository;
import com.lucy.arti.winner.repository.WinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {
    private final ClothesRepository clothesRepository;
    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    public List<Clothes> getVoteList() {
        List<Clothes> allClothes = clothesRepository.findAll();
        int clothesSize = allClothes.size();
        List<Clothes> randClothes = new ArrayList<>();
        Random randInt = new Random();
        Set<Integer> integerSet = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            while (true) {
                int newInt = randInt.nextInt(clothesSize);
                if (!(integerSet.contains(newInt))) {
                    integerSet.add(newInt);
                    randClothes.add(allClothes.get(newInt));
                    break;
                }

            }
        }
        return randClothes;
    }

    private boolean alreadyVoted(Member member) {
        LocalDateTime now = LocalDateTime.now();
        if (member.getLastVoted().getYear() == now.getYear() && member.getLastVoted().getMonth() == now.getMonth() && member.getLastVoted().getDayOfMonth() == now.getDayOfMonth()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPossibleVote(final Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).get();
        if (alreadyVoted(member)) {
            return false;
        }
        return true;
    }
    @Transactional
    public boolean vote(final Authentication authentication, VoteRequestDto voteRequestDto) {
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).get();
        if (alreadyVoted(member)) {
            return false;
        }
        member.setLastVoted();
        memberRepository.save(member);

        //////
        // 포인트 추가 로직
        Point point = pointRepository.findByMember(member);
        point.addPoint(250L);
        point.setVote(false);
        pointRepository.save(point);

        // PointHistory 생성
        PointHistory pointHistory = new PointHistory(point, "투표하기", 250L);
        pointHistoryRepository.save(pointHistory);
        //////
        for (int clothesId : voteRequestDto.getFourth()) {
            Clothes clothes = clothesRepository.findById((long) clothesId).get();
            voteRepository.save(new Vote(member, clothes, 1));
            clothes.addScore(1);
            clothesRepository.save(clothes);
        }

        int clothesId = voteRequestDto.getSecond();
        Clothes clothes = clothesRepository.findById((long) clothesId).get();
        voteRepository.save(new Vote(member, clothes, 2));
        clothes.addScore(2);
        clothesRepository.save(clothes);

        clothesId = voteRequestDto.getFirst();
        clothes = clothesRepository.findById((long) clothesId).get();
        voteRepository.save(new Vote(member, clothes, 4));
        clothes.addScore(4);
        clothesRepository.save(clothes);
        return true;
    }

}
