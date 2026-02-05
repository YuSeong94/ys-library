package com.ysk.dto; // 패키지명 확인

import lombok.Data;

@Data
// 회원 정보 변경 DTO
public class MemberUpdateDto {
    private String loginId;      // 변경 불가
    private String name;         // 변경 불가
    private String phone;        // 변경 가능
    
    // 비밀번호 변경
    private String currentPassword; // 현재 비번
    private String newPassword;     // 새 비번
    private String confirmPassword; // 새 비번 확인
}