package com.ysk.repository.community;

import com.ysk.dto.community.BoardResponseDto;
import com.ysk.entity.community.Board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

  /**
   * 페이징 처리 메서드
   * JpaRepository가 기본 제공하지만, 명시적으로 적어두면 이해하기 좋습니다.
   * Pageable(페이지 정보)을 주면, Page<Board>(페이징된 결과)를 돌려줘라
   */
  Page<Board> findAll(Pageable pageable);

  // 제목으로 검색
  Page<Board> findByTitleContaining(String Keyword, Pageable pageable);

  // 내용으로 검색
  Page<Board> findByContentContaining(String keyword, Pageable pageable);

  // 작성자로 검색
  Page<Board> findByMemberSeq_NameContaining(String keyword, Pageable pageable);



}