package com.lucy.arti.comment.controller;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.repository.ClothesRepository;
import com.lucy.arti.comment.domain.Answer;
import com.lucy.arti.comment.domain.Comment;
import com.lucy.arti.comment.dto.AnswerDto;
import com.lucy.arti.comment.dto.CommentDto;
import com.lucy.arti.comment.repository.AnswerRepository;
import com.lucy.arti.comment.repository.CommentRepository;
import com.lucy.arti.comment.service.CommentService;
import com.lucy.arti.member.domain.Member;
import com.lucy.arti.member.repository.MemberRepository;
import com.lucy.arti.pointDelivery.controller.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v2/comment")
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ClothesRepository clothesRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("/{clothesId}")
    public String createComment(@PathVariable Long clothesId, @RequestBody Map<String, String> requestBody) {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).orElseThrow(() -> new RuntimeException("Member not found"));
        String content = requestBody.get("content");
        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new RuntimeException("Clothes not found"));
        commentService.createComment(clothes,member, content);
        return "댓글이 생성되었습니다.";
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> requestBody
    ) {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        String updatedContent = requestBody.get("content");
        String updatedResult = commentService.updateComment(commentId, member, updatedContent);

        if (updatedResult.equals("success")) {
            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
        } else {
            // 에러 응답
            return ResponseEntity.badRequest().body("에러 메시지: 댓글 작성자만 댓글을 수정할 수 있습니다.");
        }
    }


    @PutMapping("/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId) {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (comment.addLike(member)) {
            commentRepository.save(comment);
            return new ResponseEntity<>("좋아요가 추가되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("이미 좋아요를 눌렀습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{commentId}/answer")
    public ResponseEntity<String> createAnswer(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> requestBody
    ) {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null) {
            return new ResponseEntity<>("Comment not found", HttpStatus.NOT_FOUND);
        }
        String content = requestBody.get("content");
        Answer saved = commentService.createAnswer(comment,content,member);
        comment.addAnswer(saved);
        commentRepository.save(comment);

        return new ResponseEntity<>("답글이 성공적으로 생성되었습니다", HttpStatus.CREATED);
    }

    @PutMapping("/answer/{answerId}/like")
    public ResponseEntity<String> likeAnswer(@PathVariable Long answerId) {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
        if (answer.addLike(member)) {
            answerRepository.save(answer);
            return new ResponseEntity<>("좋아요가 추가되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("이미 좋아요를 눌렀습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/answer/{answerId}")
    public ResponseEntity<String> updateAnswer(
            @PathVariable Long answerId,
            @RequestBody Map<String, String> requestBody
    ) {
        Authentication authentication = authenticationHelper.getAuthentication();
        long userId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByKakaoId(userId).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        String updatedContent = requestBody.get("content");
        String updatedResult = commentService.updateAnswer(answerId, member, updatedContent);

        if (updatedResult.equals("success")) {
            return ResponseEntity.ok("답글이 성공적으로 수정되었습니다.");
        } else {
            // 에러 응답
            return ResponseEntity.badRequest().body("에러 메시지: 답글 작성자만 댓글을 수정할 수 있습니다.");
        }
    }
    @GetMapping("/{clothesId}")
    public ResponseEntity<List<CommentDto>> getCommentsAndAnswers(@PathVariable Long clothesId) {
        List<Comment> comments = commentService.getCommentsByClothesId(clothesId);

        List<CommentDto> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.fromComment(comment);

            List<Answer> answers = commentService.getAnswersByCommentId(comment.getId());
            List<AnswerDto> answerDTOs = answers.stream()
                    .map(AnswerDto::fromAnswer)
                    .collect(Collectors.toList());

            commentDto.setAnswers(answerDTOs);
            commentDTOs.add(commentDto);
        }

        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }
}