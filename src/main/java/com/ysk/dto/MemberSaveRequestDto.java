package com.ysk.dto;

import com.ysk.entity.Member;
import com.ysk.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberSaveRequestDto {
  
  private String loginId;   // 아이디
  private String password;  // 비밀번호
  private String name;      // 이름
  private String phone;     // 휴대폰번호
  private String zipCode;   // 우편번호
  private String addr1;     // 기본주소
  private String addr2;     // 상세주소

  public Member toEntity(){
    
    Member member = new Member();
    
    member.setLoginId(this.loginId);
    member.setPassword(this.password);
    member.setName(this.name);
    member.setPhone(this.phone);
    member.setZipCode(this.zipCode);
    member.setAddr1(this.addr1);
    member.setAddr2(this.addr2);

    // 회원가입 시 기본 권한을 USER로 설정
    member.setRole(Role.USER);

    // 가입 시 휴먼계정 상태는 false로 설정
    member.setIsDormant(false); 

    return member;
  }
}
