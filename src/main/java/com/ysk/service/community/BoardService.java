package com.ysk.service.community;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysk.dto.community.BoardDetailDto;
import com.ysk.dto.community.BoardResponseDto;
import com.ysk.dto.community.BoardWriteDto;
import com.ysk.entity.Member;
import com.ysk.entity.community.Board;
import com.ysk.repository.MemberRepository;
import com.ysk.repository.community.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

  private final MemberRepository memberRepository;
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
  
  /**
   * 게시글 저장
   */
  public void writeSave(BoardWriteDto boardWriteDto) {
    
    // Spring Security 가 세션에서 로그인한 사용자의 ID를 가지고 온다.
    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

    // 가져온 ID로 DB에서 실제 Member 엔티티를 조회한다.
    Member member = memberRepository.findByLoginId(currentUsername)
                      .orElseThrow(() -> new UsernameNotFoundException("로그인된 사용자 정보를 찾을 수 없습니다."));

    // DTO를 Entity로 변환
    Board board = boardWriteDto.toEntity();

    // 작성자(member) 연관관계 설정
    board.setMemberSeq(member);

    // 저장
    boardRepository.save(board);
  }

  /**
   * 게시글 상세 조회
   */
  public BoardDetailDto getBoardDetail(Long id) {
    // 1. 게시글 찾기 (없으면 에러)
    Board board = boardRepository.findById(id)
                  .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

    // 2. 조회수 1 증가
    board.increaseViewCount(); 

    // 3. DTO로 변환해서 리턴
    return BoardDetailDto.fromEntity(board);
    }

  /**
   * 게시글 삭제
   */
  public void delete(Long id) {
      // 존재 여부 확인 후 삭제
      Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        
      // 삭제 실행 (DELETE FROM board WHERE board_seq = ?)
      boardRepository.delete(board);
  }
  
  /**
   * 게시글 수정
   */
  public void update(Long id, BoardWriteDto boardWriteDto) {
    // 1. 기존 글을 가져옵니다. (없으면 에러)
    Board board = boardRepository.findById(id)
                  .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

  // 2. 아까 만든 update 메서드로 내용만 쏙 바꿔치기 합니다.
  board.update(boardWriteDto.getTitle(), boardWriteDto.getContent());
  }



}
