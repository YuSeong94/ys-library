package com.ysk.dto.community;

import java.time.LocalDateTime;

import com.ysk.entity.community.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDto {
    
    private Long replySeq;      // 댓글 번호
    private Long boardSeq;      // 게시글 번호
    private Long memberSeq;     // 작성자 PK (권한 체크용)
    private String writerName;  // 작성자 이름 (화면 표시용)
    private String content;     // 댓글 내용
    private LocalDateTime regDatetime; // 작성일자
    private LocalDateTime modDatetime; // 수정일자

    // Reply 엔티티를 받아서 DTO로 변환
    public static ReplyDto fromEntity(Reply reply) {
        return ReplyDto.builder()
                .replySeq(reply.getReplySeq())
                .boardSeq(reply.getBoard().getBoardSeq())
                .memberSeq(reply.getMember().getMemberSeq())
                .writerName(reply.getMember().getName()) // Member 엔티티의 name 사용
                .content(reply.getContent())
                .regDatetime(reply.getRegDatetime())
                .modDatetime(reply.getModDatetime())
                .build();
    }
}