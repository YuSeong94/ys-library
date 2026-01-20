package com.ysk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean // 이 메소드가 반환하는 객체(PasswordEncoder)를 스프링이 관리하는 Bean으로 등록합니다.
    public PasswordEncoder passwordEncoder() {
        // BCrypt는 현재 가장 널리 사용되는 안전한 암호화 방식 중 하나입니다.
        return new BCryptPasswordEncoder();
    }
    
    // 지금 당장은 모든 요청을 허용하도록 설정합니다. (로그인 기능 구현 시 수정 예정)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .formLogin(form -> form.disable()) // 스프링 시큐리티의 기본 로그인 폼 비활성화
            .csrf(csrf -> csrf.disable()); // CSRF 보호 기능 비활성화 (개발 편의를 위해)
        return http.build();
    }

}
