package com.lucy.arti.vote.repository;

import com.lucy.arti.member.domain.Member;
import com.lucy.arti.vote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom{

    List<Vote> findAllByMemberAndScore(Member member, int score);
}
