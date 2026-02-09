package com.ysk.service.community;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysk.dto.community.BoardResponseDto;
import com.ysk.entity.community.Board;
import com.ysk.repository.community.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  /**
   * 게시글 목록 조회 
   * 페이징 적용
   * 게시글 검색 기능 적용
   * Pageable: 몇 페이지, 몇 개씩, 정렬 정보가 들어있음 
   * Page<DTO>: 해당 페이지의 데이터+페이징 정보 반환
   * @param pageable
   * @return
   */
  public Page<BoardResponseDto> getBoardList(String searchType, String keyword, Pageable pageable) {
    Page<Board> boardPage;

    // 검색어가 없을 경우 전체 조회
    if(keyword == null || keyword.trim().isEmpty()){
      boardPage = boardRepository.findAll(pageable);
    
    // 검색어가 있을 경우 타입별로 체크
    } else {
      if("title".equals(searchType)){
        boardPage = boardRepository.findByTitleContaining(keyword, pageable);
      } else if("content".equals(searchType)){
        boardPage = boardRepository.findByContentContaining(keyword, pageable);
      } else if("writer".equals(searchType)){
        boardPage = boardRepository.findByMemberSeq_NameContaining(keyword, pageable);
      } else {
        // 예외로 타입이 이상하면 전체 조회
        boardPage = boardRepository.findAll(pageable);
      }
    }
    return boardPage.map(BoardResponseDto::new);
    }

}
