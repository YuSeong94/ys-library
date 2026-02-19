package com.ysk.controller.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ysk.dto.community.BoardDetailDto;
import com.ysk.dto.community.BoardResponseDto;
import com.ysk.dto.community.BoardWriteDto;
import com.ysk.entity.Member;
import com.ysk.service.community.BoardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/community/board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  /**
   * 게시판 페이지로 이동
   */
  @GetMapping("list")
  public String list(Model model, @PageableDefault(page = 0, size = 10, sort = "boardSeq",
    direction = Sort.Direction.DESC) Pageable pageable,
    @RequestParam(name = "searchType", required = false) String searchType,
    @RequestParam(name = "keyword", required = false) String keyword){
    
    Page<BoardResponseDto> list = boardService.getBoardList(searchType, keyword, pageable);

    int nowPage = list.getPageable().getPageNumber() + 1; // 현재 페이지
    int totalPages = list.getTotalPages(); // 전체 페이지 수
    int pageBlock = 10; // 블록 크기

    // 1. 시작 페이지 계산
    // (현재페이지-1) / 블록크기 * 블록크기 + 1
    // 예: 5페이지 -> (4/10)*10 + 1 = 0 + 1 = 1
    // 예: 11페이지 -> (10/10)*10 + 1 = 10 + 1 = 11
    int startPage = ((nowPage - 1) / pageBlock) * pageBlock + 1;

    // 2. 끝 페이지 계산
    // 시작페이지 + 블록크기 - 1
    // 예: 1 + 10 - 1 = 10
    int endPage = startPage + pageBlock - 1;

    // 3. 실제 마지막 페이지보다 endPage가 크면 안 됨
    // 전체가 42페이지인데 endPage가 50이면 -> 42로 강제 조정
    if (endPage > totalPages) {
        endPage = totalPages;
    }

    // 이전/다음 '구간' 계산 로직
        
    // [<<] 이전 블록 가기: 현재 시작 페이지가 1보다 크면 있음
    // 예: 11페이지라면 -> 10페이지로 이동 (1~10구간의 마지막)
    boolean hasPrevBlock = startPage > 1;
    int prevBlockPage = startPage - 1; // 11 -> 10, 21 -> 20

    // [>>] 다음 블록 가기: 현재 끝 페이지가 전체 페이지보다 작으면 있음
    // 예: 10페이지라면 -> 11페이지로 이동 (11~20구간의 시작)
    boolean hasNextBlock = endPage < totalPages;
    int nextBlockPage = endPage + 1; // 10 -> 11, 20 -> 21

    model.addAttribute("list", list);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("hasPrevBlock", hasPrevBlock);
    model.addAttribute("prevBlockPage", prevBlockPage);
    model.addAttribute("hasNextBlock", hasNextBlock);
    model.addAttribute("nextBlockPage", nextBlockPage);
    model.addAttribute("searchType", searchType);
    model.addAttribute("keyword", keyword);

    return "community/board/list";
  }

  /**
   * 게시글 작성 페이지로 이동
   */
  @GetMapping("/write")
  public String goWriteForm(){
    return "community/board/write";
  }

  /**
   * 게시글 저장
   */
  @PostMapping("/write")
  public String writeSave(BoardWriteDto boardWriteDto) {
    boardService.writeSave(boardWriteDto);

    return "redirect:/community/board/list";
  }

  /**
   * 게시글 상세 페이지 이동
   */
  @GetMapping("/view/{boardSeq}")
  public String view(@PathVariable(name = "boardSeq") Long boardSeq, Model model) {
    BoardDetailDto boardDetail = boardService.getBoardDetail(boardSeq);
        
    model.addAttribute("board", boardDetail);
        
    return "community/board/view";
  }

  /**
   * 게시글 삭제 요청 처리
   */
  @GetMapping("/delete/{boardSeq}")
  public String delete(@PathVariable(name = "boardSeq") Long boardSeq, Model model, HttpSession session) {
        
  BoardDetailDto board = boardService.getBoardDetail(boardSeq);
  Member loginMember = (Member) session.getAttribute("loginMember");

  if (loginMember == null || !board.getWriterSeq().equals(loginMember.getMemberSeq())) {
    model.addAttribute("message", "삭제 권한이 없습니다.");
    model.addAttribute("searchUrl", "/community/board/view/" + boardSeq);
    return "common/message";
  }

  boardService.delete(boardSeq);

  model.addAttribute("message", "게시글이 삭제되었습니다.");
  model.addAttribute("searchUrl", "/community/board/list");
  return "common/message";
  }

  /**
   * 수정 페이지 이동
   */
  @GetMapping("/modify/{boardSeq}")
  public String modifyForm(@PathVariable(name = "boardSeq") Long boardSeq, Model model, HttpSession session) {
    BoardDetailDto boardDetail = boardService.getBoardDetail(boardSeq);
      
    Member loginMember = (Member)session.getAttribute("loginMember");

    if (loginMember == null || !boardDetail.getWriterSeq().equals(loginMember.getMemberSeq())) {
      model.addAttribute("message", "수정 권한이 없습니다.");
      model.addAttribute("searchUrl", "/community/board/view/" + boardSeq); // 다시 상세 페이지로
      return "common/message";
    }

    model.addAttribute("board", boardDetail);
    return "community/board/modify";
  }

  /**
   * 게시글 수정 기능
   */
  @PostMapping("/modify/{boardSeq}")
  public String modify(@PathVariable(name = "boardSeq") Long boardSeq, BoardWriteDto boardWriteDto, 
                        Model model, HttpSession session) {
    
    // 게시글 정보 조회
    BoardDetailDto board = boardService.getBoardDetail(boardSeq);
    Member loginMember = (Member) session.getAttribute("loginMember");

    if (loginMember == null || !board.getWriterSeq().equals(loginMember.getMemberSeq())) {
      model.addAttribute("message", "수정 권한이 없습니다.");
      model.addAttribute("searchUrl", "/community/board/view/" + boardSeq);
      return "common/message";
    }

    boardService.update(boardSeq, boardWriteDto);
    
    model.addAttribute("message", "게시글이 수정되었습니다.");
    model.addAttribute("searchUrl", "/community/board/view/" + boardSeq);
    return "common/message";
  }

}
