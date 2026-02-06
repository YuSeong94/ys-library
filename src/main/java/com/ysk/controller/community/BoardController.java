package com.ysk.controller.community;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ysk.dto.community.BoardResponseDto;
import com.ysk.entity.Member;
import com.ysk.entity.community.Board;
import com.ysk.repository.MemberRepository;
import com.ysk.repository.community.BoardRepository;
import com.ysk.service.community.BoardService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;


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
    direction = Sort.Direction.DESC) Pageable pageable) {
    
    Page<BoardResponseDto> list = boardService.getBoardList(pageable);

    int nowPage = list.getPageable().getPageNumber() + 1; // 현재 페이지 (1부터 시작)
    int totalPages = list.getTotalPages(); // 전체 페이지 수 (예: 40페이지)
        
    int pageBlock = 10; // 📢 블록의 크기 (1~10, 11~20 처럼 10개씩 보여줌)

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

    model.addAttribute("list", list);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    return "community/board/list";
  }




}
