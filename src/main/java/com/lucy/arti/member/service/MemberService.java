package com.lucy.arti.member.service;

import static com.lucy.arti.exception.MemberException.DUPLICATE_NICKNAME;

import com.lucy.arti.clothes.dto.ClothesDetailResponseDto;
import com.lucy.arti.exception.ErrorResult;
import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import com.lucy.arti.global.util.S3Manager;
import com.lucy.arti.like.domain.Like;
import com.lucy.arti.like.repository.LikeRepository;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.dto.MemberPictureResponseDto;
import com.lucy.arti.member.dto.MemberUpdateResponseDto;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.vote.domain.Vote;
import com.lucy.arti.vote.repository.VoteRepository;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final LikeRepository likeRepository;
    private final S3Manager s3Manager;
    public List<ClothesDetailResponseDto> victory(final Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).get();
        List<Vote> allVotes = voteRepository.findAllByMemberAndScore(member,4);
        List<ClothesDetailResponseDto> allClothes = new ArrayList<>();
        for (Vote vote : allVotes) {
            allClothes.add(ClothesDetailResponseDto.of(vote.getClothes(), vote.getClothes().getDesigner()));
        }
        return allClothes;
    }

    public List<ClothesDetailResponseDto> saveListShow(final Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 kakaoId를 가진 회원을 조회할 수 없습니다."));
        List<Like> allLikes = likeRepository.findAllByMemberId(member.getId());
        List<ClothesDetailResponseDto> allClothes = new ArrayList<>();
        for (Like likes : allLikes) {
            allClothes.add(ClothesDetailResponseDto.of(likes.getClothes(), likes.getClothes().getDesigner()));
        }
        return allClothes;
    }

    public MemberPictureResponseDto updatePicture(final Authentication authentication,
        MultipartFile image) throws IOException {

        Member member = memberRepository.findByKakaoIdOrThrow(
            Long.parseLong(authentication.getName()));

        String customProfileUrl = s3Manager.upload(image,"profile");
        member.updateProfile(customProfileUrl);
        Member saved = memberRepository.save(member);

        return new MemberPictureResponseDto(
            saved.getId(),
            saved.getCustomProfile()
        );
    }

    public MemberUpdateResponseDto updateNickname(final Authentication authentication, String customName) {
        long kakaoId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoIdOrThrow(kakaoId);

        if (customName.length() > 10) {
            throw BusinessException.from(ErrorCode.MEMBER_NICKNAME_LENGTH);
        }

        if (memberRepository.existsByNickname(customName)) {
            throw BusinessException.from(ErrorCode.MEMBER_DUPLICATE_NICKNAME);
        }
        member.updateNickName(customName);
        Member updated = memberRepository.save(member);

        return new MemberUpdateResponseDto(
            updated.getId(),
            updated.getKakaoId(),
            updated.getNickname()
        );
    }

    public String generateRandomNickName() {
        enum Adjective {
            헬스하는, 독서하는, 팝콘먹는, 라면먹는, 사과깎는, 귤먹는,
            영화보는, 스키타는, 농사짓는, 코딩하는, 요가하는, 명상하는, 멍때리는
        }

        enum Animal {
            코끼리, 곰, 도마뱀, 뱀, 코알라, 캥거루, 홍학, 황새,
            물고기, 고래, 미어캣, 펭귄, 판다, 오징어, 원숭이,
            고릴라, 얼룩말, 청설모, 다람쥐, 푸들, 불독, 치와와,
            수달, 해달, 토끼, 햄스터, 독수리, 까치, 참새, 하마, 기린,
            개구리, 두꺼비, 염소, 코뿔소, 거북이, 너구리, 앵무새
        }

        return String.format("%s%s%s", getRandomEnum(Adjective.class),
            getRandomEnum(Animal.class), generateCode());
    }

    private <T extends Enum<?>> T getRandomEnum(Class<T> enumClass) {
        Random random = new Random();
        T[] enums = enumClass.getEnumConstants();
        return enums[random.nextInt(enums.length)];
    }

    private String generateCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        return uuid.substring(0, 2);
    }

}
