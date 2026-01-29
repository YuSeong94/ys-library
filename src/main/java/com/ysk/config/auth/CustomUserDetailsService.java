package com.ysk.config.auth;

import com.ysk.entity.Member;
import com.ysk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  /**
   * 스프링 시큐리티가 로그인 요청을 가로챌 때, DB에서 해당 회원이 있는지 확인하는 메서드
   */
  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
      
      // DB에서 아이디로 회원 조회
      Member member = memberRepository.findByLoginId(loginId)
              .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다: " + loginId));

      // 스프링 시큐리티는 Member를 모르고 UserDetails만 알기 때문에 Member 객체를 아까 만든 CustomUserDetails에 담아서 리턴한다.
      return new CustomUserDetails(member);
  }
}