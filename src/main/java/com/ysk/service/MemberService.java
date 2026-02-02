package com.ysk.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysk.dto.MemberSaveRequestDto;
import com.ysk.dto.MemberUpdateDto;
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
  @Transactional
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

  /**
   * 회원 정보 수정
   */
  @Transactional 
  public void updateMember(Long memberSeq, MemberUpdateDto dto) {
        
    // 1. 회원 조회
    Member member = memberRepository.findById(memberSeq)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    // 2. 전화번호 변경
    if (dto.getPhone() != null) {
        member.setPhone(dto.getPhone());
    }

    // 3. 비밀번호 변경 (새 비밀번호 입력칸에 값이 있을 경우에만 실행)
    if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {

        // 3-1. 현재 비밀번호가 맞는지 확인 (DB에 있는 암호화된 비번 vs 입력한 비번 비교)
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 3-2. 새 비밀번호와 확인 비밀번호가 서로 같은지 확인
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalStateException("새 비밀번호가 일치하지 않습니다.");
        }

        // 3-3. 모든 검증 통과 시 암호화하여 저장
        member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }
  }




}
