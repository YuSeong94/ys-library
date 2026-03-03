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
  private boolean isEdited;   // 수정여부

  // Reply 엔티티를 받아서 DTO로 변환
  public static ReplyDto fromEntity(Reply reply) {

    boolean edited = false;
    if (reply.getModDatetime() != null && reply.getRegDatetime() != null) {
      // 수정일이 작성일보다 이후라면 (보통 수정 발생 시 갱신됨)
      edited = reply.getModDatetime().isAfter(reply.getRegDatetime());
    }

    return ReplyDto.builder()
            .replySeq(reply.getReplySeq())
            .boardSeq(reply.getBoard().getBoardSeq())
            .memberSeq(reply.getMember().getMemberSeq())
            .writerName(reply.getMember().getName())
            .content(reply.getContent())
            .regDatetime(reply.getRegDatetime()) // 작성일자 추가
            .modDatetime(reply.getModDatetime()) // 수정일자 추가
            .isEdited(edited)                    // 계산된 수정 여부 추가
            .build();
  }
}