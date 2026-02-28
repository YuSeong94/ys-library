package com.ysk.service.community;

import com.ysk.dto.community.ReplyDto;
import com.ysk.dto.community.ReplyModifyRequestDto;
import com.ysk.entity.Member;
import com.ysk.entity.community.Board;
import com.ysk.entity.community.Reply;
import com.ysk.repository.community.BoardRepository;
import com.ysk.repository.community.ReplyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository; 

    /**
     * 1. 댓글 등록
     * (Controller에서 세션에 있는 loginMember를 통째로 넘겨받습니다)
     */
    public Long saveReply(Long boardSeq, Member loginMember, String content) {
        // 1. 어느 게시글인지 DB에서 찾기
        Board board = boardRepository.findById(boardSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. 댓글 엔티티 생성
        Reply reply = Reply.builder()
                .content(content)
                .board(board)
                .member(loginMember) // 세션에서 가져온 멤버 객체 그대로 연결!
                .build();

        // 3. DB 저장
        replyRepository.save(reply);
        return reply.getReplySeq();
    }

    /**
     * 2. 특정 게시글의 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ReplyDto> getReplies(Long boardSeq) {
        // DB에서 등록일자 오름차순으로 댓글 엔티티들을 가져옴
        List<Reply> replies = replyRepository.findByBoard_BoardSeqOrderByRegDatetimeAsc(boardSeq);
        
        // Entity 리스트를 DTO 리스트로 변환 (아까 만든 fromEntity 활용)
        return replies.stream()
                .map(ReplyDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 3. 댓글 삭제 (🚨 백엔드 이중 보안 적용)
     */
    public void deleteReply(Long replySeq, Long loginMemberSeq) {
        Reply reply = replyRepository.findById(replySeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 권한 체크: DB에 저장된 작성자 PK와 현재 로그인한 사람의 PK가 같은지 비교!
        if (!reply.getMember().getMemberSeq().equals(loginMemberSeq)) {
            throw new IllegalStateException("댓글 삭제 권한이 없습니다.");
        }

        replyRepository.delete(reply);
    }

    public void modifyReply(ReplyModifyRequestDto dto, Long loginMemberSeq) {
        // 1. 기존 댓글 조회
        Reply reply = replyRepository.findById(dto.getReplySeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 2. 권한 검증 (수정 요청한 사람이 진짜 작성자인지 확인)
        if (!reply.getMember().getMemberSeq().equals(loginMemberSeq)) {
            throw new IllegalStateException("본인의 댓글만 수정할 수 있습니다.");
        }

        // 3. 댓글 내용 변경 (이게 끝입니다! save 안 해도 됨)
        reply.update(dto.getContent()); 
    }


    
}