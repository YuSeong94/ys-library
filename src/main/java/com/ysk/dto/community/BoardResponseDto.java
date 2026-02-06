package com.ysk.dto.community;

import com.ysk.entity.community.Board;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
// 게시판 목록 DTO
public class BoardResponseDto {

    private Long boardSeq;      // 게시글 번호
    private String title;       // 제목
    private String content;     // 내용
    private String writer;      // 작성자 이름
    private int viewCount;      // 조회수
    private String regDate;     // 작성일 (문자열로 예쁘게 변환)
    private boolean newArticle; // 새로운 게시글 판단



    // Entity -> DTO 변환 생성자
    public BoardResponseDto(Board board) {
        this.boardSeq = board.getBoardSeq(); // 사용자가 정한 변수명 boardSeq
        this.title = board.getTitle();
        this.content = board.getContent();
        this.viewCount = board.getViewCount();
        
        // 작성자 꺼내기
        if (board.getMemberSeq() != null) {
            this.writer = board.getMemberSeq().getName(); // 아이디를 쓰고 싶으면 .getLoginId() 등을 사용
        } else {
            this.writer = "탈퇴회원";
        }

        // 날짜 포맷팅
        if (board.getRegDatetime() != null) {
          this.regDate = board.getRegDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

          // 오늘 작성된 글인지 체크
          // 작성일(LocalDate)과 오늘날짜(LocalDate.now())가 같으면 true 반환
          LocalDate createDate = board.getRegDatetime().toLocalDate();
          LocalDate today = LocalDate.now();

          this.newArticle = createDate.isEqual(today);
        }
    }
}