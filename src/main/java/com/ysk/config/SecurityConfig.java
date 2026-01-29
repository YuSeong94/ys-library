package com.ysk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    
  @Bean // 이 메소드가 반환하는 객체(PasswordEncoder)를 스프링이 관리하는 Bean으로 등록합니다.
  public PasswordEncoder passwordEncoder() {
    // BCrypt는 현재 가장 널리 사용되는 안전한 암호화 방식 중 하나입니다.
    return new BCryptPasswordEncoder();
  }
    
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
          // 1. 페이지 접근 권한 설정 (인가)
          .authorizeHttpRequests(auth -> auth
              // css, js, images 등 정적 리소스는 누구나 접근 가능
              .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
              // 메인 페이지, 회원가입, 로그인 페이지는 누구나 접근 가능
              .requestMatchers("/","/members/join", "/members/new", "/members/login", "/common/**").permitAll()
              // 그 외의 모든 페이지는 로그인한 사람만 접근 가능 (나중에 게시판 등)
              .anyRequest().authenticated()
          )

          // 2. 로그인 설정
          .formLogin(form -> form
              .loginPage("/members/login")       // 우리가 만든 로그인 페이지 URL
              .loginProcessingUrl("/members/login") // 로그인 Form의 action URL (스프링이 가로챔)
              .usernameParameter("loginId")      // 중요! HTML input name이 "loginId"이므로 설정 필수 (기본값: username)
              .passwordParameter("password")     // HTML input name이 "password" (기본값과 같아서 생략 가능)
              .defaultSuccessUrl("/", true)      // 로그인 성공 시 이동할 페이지 (메인)
              .permitAll()
          )

          // 3. 로그아웃 설정
          .logout(logout -> logout
              .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL
              .logoutSuccessUrl("/")             // 로그아웃 성공 시 메인으로 이동
              .invalidateHttpSession(true)       // 세션 삭제
              .deleteCookies("JSESSIONID")       // 쿠키 삭제
              .permitAll()
          );

      return http.build();
  }

}
