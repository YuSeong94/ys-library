package com.ysk.dto.community;

import com.ysk.entity.community.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@ToString
@NoArgsConstructor
public class BoardDetailDto {

    private Long boardSeq;      // 게시글 번호
    private String title;       // 제목
    private String content;     // 본문 내용
    private String writer;      // 작성자 이름
    private Long writerSeq;     // 작성자 회원번호 (수정/삭제 권한 체크)
    private int viewCount;      // 조회수
    private String regDate;     // 작성일

    // Entity -> DTO 변환
    public static BoardDetailDto fromEntity(Board board) {
        BoardDetailDto dto = new BoardDetailDto();
        dto.boardSeq = board.getBoardSeq();
        dto.title = board.getTitle();
        dto.content = board.getContent();
        dto.viewCount = board.getViewCount();
        
        // 작성자 정보 (Member 엔티티 연결)
        if (board.getMemberSeq() != null) {
            dto.writer = board.getMemberSeq().getName(); // 이름
            dto.writerSeq = board.getMemberSeq().getMemberSeq(); // PK (나중에 씀!)
        }

        // 날짜 포맷
        if (board.getRegDatetime() != null) {
            dto.regDate = board.getRegDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        
        return dto;
    }
}