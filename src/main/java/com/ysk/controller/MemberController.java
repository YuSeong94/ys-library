package com.ysk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ysk.dto.MemberSaveRequestDto;
import com.ysk.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
  
  private final MemberService memberService;

  /**
   * 로그인 페이지 이동
   * @return
   */
  @GetMapping("/login")
  public String goLoginPage(){
    return "members/login";
  }

  /**
   * 회원가입 페이지 이동
   * @return
   */
  @GetMapping("/join")
  public String goJoinPage(Model model){
    // 빈 객체를 보내서 Thymeleaf가 인식하게 함
    model.addAttribute("memberSaveRequestDto", new MemberSaveRequestDto());
    return "members/join";
  }

  /**
   * 회원가입 
   * @param memberDto
   * @param bindingResult
   * @param model
   * @return
   */
  @PostMapping("/new")
  public String join(@Valid MemberSaveRequestDto memberDto, BindingResult bindingResult, Model model) {
    
    System.out.println("memberDto : " + memberDto);
    System.out.println("bindingResult : " + bindingResult);
    System.out.println("model : " + model);

    // 1. 입력값 검증 실패 시 -> 다시 가입 페이지로 이동
    if(bindingResult.hasErrors()){
      return "members/join";
    }

    try {
      // 2. 정상 로직 -> 서비스 호출
      memberService.join(memberDto);
    } catch (Exception e) {
      // 3. 예외 발생 시 -> 에러 메세지를 Model에 담아 다시 가입페이지로 이동
      model.addAttribute("errorMessage", e.getMessage());
      return "members/join";
    }

    // 4. 회원가입 성공 시 -> 로그인페이지 이동
    model.addAttribute("message", "회원가입이 완료되었습니다.");
    model.addAttribute("searchUrl", "/members/login");
    return "common/message";
  }

}
