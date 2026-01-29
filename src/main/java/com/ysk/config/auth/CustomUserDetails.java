package com.ysk.config.auth;

import com.ysk.entity.Member;
import com.ysk.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  // 우리의 진짜 회원 정보
  private final Member member; 

  // 권한 목록 리턴
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    
    // Enum으로 저장된 Role을 스프링 시큐리티가 인식하도록 "ROLE_" 접두사를 붙여서 변환 (Role.USER -> "ROLE_USER")
    authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    
    return authorities;
  }

  // 비밀번호 리턴 (로그인 시 검증용)
  @Override
  public String getPassword() {
    return member.getPassword();
  }

  // 로그인 아이디 리턴
  @Override
  public String getUsername() {
    return member.getLoginId();
  }

  // 계정 상태 여부
  @Override
  public boolean isAccountNonExpired() {
    return true; // 계정 만료 안됨
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // 계정 잠김 아님
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // 비밀번호 만료 안됨
  }

  @Override
  public boolean isEnabled() {
    // 나중에 휴면 계정(isDormant) 체크를 여기서 할 수 있음
    // 지금은 true(사용 가능)로 리턴
    return true; 
  }
}