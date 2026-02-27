package com.ysk.controller.community;

import com.ysk.dto.community.ReplyDto;
import com.ysk.dto.community.ReplyModifyRequestDto;
import com.ysk.entity.Member;
import com.ysk.service.community.ReplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/reply")
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 1. 댓글 쓰기 (POST)
     * 화면에서 Ajax로 넘어온 JSON 데이터를 @RequestBody로 받습니다.
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveReply(@RequestBody Map<String, String> payload, HttpSession session) {
        // 어제 만든 세션에서 로그인 정보 꺼내기!
        Member loginMember = (Member) session.getAttribute("loginMember");
        
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스입니다.");
        }

        Long boardSeq = Long.valueOf(payload.get("boardSeq"));
        String content = payload.get("content");

        // Service 호출
        replyService.saveReply(boardSeq, loginMember, content);
        return ResponseEntity.ok("댓글이 성공적으로 등록되었습니다.");
    }

    /**
     * 2. 댓글 목록 불러오기 (GET)
     * 특정 게시글(boardSeq)에 달린 댓글들을 JSON 리스트로 반환합니다.
     */
    // 💡 수정됨: name = "boardSeq" 명시!
    @GetMapping("/list/{boardSeq}")
    public ResponseEntity<List<ReplyDto>> getReplies(@PathVariable(name = "boardSeq") Long boardSeq) {
        List<ReplyDto> replies = replyService.getReplies(boardSeq);
        return ResponseEntity.ok(replies);
    }

    /**
     * 3. 댓글 삭제 (DELETE)
     * 이중 보안: 세션의 사용자 PK와 댓글 작성자 PK를 Service에서 비교합니다.
     */
    // 💡 수정됨: name = "replySeq" 명시!
    @DeleteMapping("/delete/{replySeq}")
    public ResponseEntity<String> deleteReply(@PathVariable(name = "replySeq") Long replySeq, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            // Service로 댓글 번호와 현재 로그인한 사람의 PK를 같이 넘김
            replyService.deleteReply(replySeq, loginMember.getMemberSeq());
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (IllegalStateException e) {
            // Service에서 권한이 없다고 예외를 던지면 여기서 받아서 처리!
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    @PutMapping("/community/reply/modify")
    @ResponseBody
    public ResponseEntity<String> modifyReply(@RequestBody ReplyModifyRequestDto dto, HttpSession session) {
        // 1. 로그인 확인
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(403).body("로그인이 필요합니다.");
        }

        try {
            // 2. 서비스 호출
            replyService.modifyReply(dto, loginMember.getMemberSeq());
            return ResponseEntity.ok("수정 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}