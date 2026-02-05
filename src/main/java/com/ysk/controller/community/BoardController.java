package com.ysk.controller.community;

import java.util.List;
import java.util.Map;

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
  public String list(Model model) {
    List<BoardResponseDto> boardList = boardService.getBoardListAll();
    model.addAttribute("boardList", boardList);
    return "community/board/list";
  }




}
