package com.ysk.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/community/board")
public class BoardController {
  
  @GetMapping("list")
  public String list() {
      return "community/board/list";
  }
  


}
