package com.ysk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
  
  /**
   * 로그인 페이지 이동
   * @return
   */
  @GetMapping("/login")
  public String loginForm(){
    return "members/login";
  }

  /**
   * 회원가입 페이지 이동
   * @return
   */
  @GetMapping("/join")
  public String joinForm(){
    return "members/join";
  }



}
