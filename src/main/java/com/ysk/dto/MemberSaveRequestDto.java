package com.ysk.dto;

import com.ysk.entity.Member;
import com.ysk.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Setter
@Getter
// 회원가입 DTO
public class MemberSaveRequestDto {
  
  @NotBlank(message = "아이디는 필수 입력 값입니다.")
  private String loginId;   // 아이디

  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  @Size(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
  private String password;  // 비밀번호

  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String name;      // 이름

  private String phone;     // 휴대폰번호
  private String zipCode;   // 우편번호
  private String addr1;     // 기본주소
  private String addr2;     // 상세주소
  
  public Member toEntity(PasswordEncoder passwordEncoder){
    
    Member member = new Member();
    
    member.setLoginId(this.loginId);
    
    // 암호화 저장
    member.setPassword(passwordEncoder.encode(this.password));
    
    member.setName(this.name);
    member.setPhone(this.phone);
    member.setZipCode(this.zipCode);
    member.setAddr1(this.addr1);
    member.setAddr2(this.addr2);

    // 회원가입 시 기본 권한을 USER로 설정
    member.setRole(Role.USER);

    // 가입 시 휴면계정 상태는 false로 설정
    member.setIsDormant(false); 

    return member;
  }
}