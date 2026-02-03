package com.ysk.controller;

import org.springframework.security.access.method.P;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ysk.config.auth.CustomUserDetails;
import com.ysk.dto.MemberSaveRequestDto;
import com.ysk.dto.MemberUpdateDto;
import com.ysk.entity.Member;
import com.ysk.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
   */
  @GetMapping("/login")
  public String goLoginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @CookieValue(value = "savedId", required = false) String savedId, // 쿠키가 있으면 가져오고, 없으면 null
                            Model model){

    if (error != null) {
        model.addAttribute("error", "아이디 또는 비밀번호를 확인해주세요.");
      }
      if (logout != null) {
        model.addAttribute("logout", "로그아웃 되었습니다.");
      }

      // 쿠키에서 꺼낸 아이디를 model에 저장
      if (savedId != null) {
        model.addAttribute("savedId", savedId);
      }

    return "members/login";
  }

  /**
   * 회원가입 페이지 이동
   */
  @GetMapping("/join")
  public String goJoinPage(Model model){
    // 빈 객체를 보내서 Thymeleaf가 인식하게 함
    model.addAttribute("memberSaveRequestDto", new MemberSaveRequestDto());
    return "members/join";
  }

  /**
   * 회원가입 
   */
  @PostMapping("/new")
  public String join(@Valid MemberSaveRequestDto memberDto, BindingResult bindingResult, Model model) {
    
    // 데이터 확인
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

  // 정보수정 페이지로 이동
  @GetMapping("/edit")
  public String goEditPage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    
    // 로그인 체크 (세션 만료 시 로그인 페이지로)
    if(customUserDetails == null){
      return "redirect:/members/login";
    }
    
    // CustomUserDetails 안에 있는 Member 정보 가져오기
    Member member = customUserDetails.getMember();

    MemberUpdateDto updateDto = new MemberUpdateDto();
    updateDto.setLoginId(member.getLoginId());
    updateDto.setName(member.getName());
    updateDto.setPhone(member.getPhone());

    model.addAttribute("memberUpdateDto", updateDto);

    return "members/edit";
  }
  
  /**
   * 회원 정보 수정
   */
  @PostMapping("/edit")
  public String edit(@AuthenticationPrincipal CustomUserDetails customUserDetails,
    MemberUpdateDto updateDto, Model model, HttpServletRequest request) {
      
      try {
        memberService.updateMember(customUserDetails.getMember().getMemberSeq(), updateDto);
        
        HttpSession session = request.getSession(false);
        if(session != null){
          session.invalidate();
        }
        
        // 서비스 호출 (updateDto 안에 phone, newPassword 등이 들어있음)
        model.addAttribute("message", "정보가 변경되었습니다. 다시 로그인해주세요.");
        model.addAttribute("searchUrl", "/login");
            
        // 3. message.html 템플릿 호출
        return "common/message";

      } catch (Exception e) {
        // 예외 발생 시 (비밀번호 불일치 등)
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("memberUpdateDto", updateDto); 
        return "members/edit";
      }
  }
  
  /**
   * 회원 탈퇴 처리
   */
  @GetMapping("/delete")
  public String deleteMember(@AuthenticationPrincipal CustomUserDetails customUserDetails,
      HttpServletRequest request, Model model) {
      
      // 1. 회원 삭제
      memberService.withdraw(customUserDetails.getMember().getMemberSeq());

      // 2. 세션 삭제 (로그아웃)
      HttpSession session = request.getSession(false);
      if (session != null) {
          session.invalidate();
      }

      // 3. 메시지 띄우고 메인으로 이동
      model.addAttribute("message", "회원 탈퇴가 완료되었습니다. 그동안 이용해주셔서 감사합니다.");
      model.addAttribute("searchUrl", "/");
      
      return "common/message";
  }







}
