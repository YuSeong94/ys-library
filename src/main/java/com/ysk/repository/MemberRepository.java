package com.ysk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysk.entity.Member;

/**
 * Member 엔터티에 대한 데이터 접근을 처리하는 Repository 인터페이스
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
  
  // 아이디로 회원 조회
  Optional<Member> findByLoginId(String loginId);

  // 회원가입 시 아이디 중복 체크
  boolean existsByLoginId(String loginId);

}
