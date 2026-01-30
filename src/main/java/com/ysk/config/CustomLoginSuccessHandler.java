package com.ysk.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        // 1. HTML 폼에서 넘어온 데이터 확인
        String saveId = request.getParameter("saveId"); // 체크박스 값 ("on" 또는 null)
        String loginId = request.getParameter("loginId"); // 입력한 아이디

        // 2. 체크박스가 켜져 있다면 ("on")
        if (saveId != null) {
            // 쿠키 생성
            Cookie cookie = new Cookie("savedId", loginId);
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30일 유지
            cookie.setPath("/"); // 모든 페이지에서 유효
            response.addCookie(cookie);
        } else {
            // 3. 체크박스가 꺼져 있다면 (쿠키 삭제)
            Cookie cookie = new Cookie("savedId", null);
            cookie.setMaxAge(0); // 수명을 0으로 해서 즉시 삭제
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // 4. 할 일 다 했으니 메인 페이지로 이동
        setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}