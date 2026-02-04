package com.ysk.repository.community;

import com.ysk.entity.community.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 페이징 처리 메서드
    // JpaRepository가 기본 제공하지만, 명시적으로 적어두면 이해하기 좋습니다.
    // Pageable(페이지 정보)을 주면, Page<Board>(페이징된 결과)를 돌려줘라
    Page<Board> findAll(Pageable pageable);

}