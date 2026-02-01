package com.ysk.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysk.dto.MemberSaveRequestDto;
import com.ysk.entity.Member;
import com.ysk.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
  
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 회원가입
   * @param memberDto
   * @return 회원 Seq
   */
  public Long join(MemberSaveRequestDto memberDto) {
    // 1. 중복 아이디 검증
    validateDuplicateMember(memberDto.getLoginId());

    // 2. DTO를 엔티티로 변환 (비밀번호 암호화)
    Member member = memberDto.toEntity(passwordEncoder);

    // 3. DB 저장
    memberRepository.save(member);

    return member.getMemberSeq();
  }

  // 중복 검증 메서드 추출
  private void validateDuplicateMember(String loginId) {
    if (memberRepository.existsByLoginId(loginId)) {
    throw new IllegalStateException("이미 존재하는 아이디입니다."); 
    }
  }

  @Transactional(readOnly = true)
  public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 회원이 없습니다."));
  }

}
