package com.ysk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  
  @GetMapping("/")
  public String home(Model model) {
      model.addAttribute("title", "송월 나주도서관");
      return "index";
  }
}
