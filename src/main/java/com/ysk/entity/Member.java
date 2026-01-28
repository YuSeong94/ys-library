package com.ysk.entity;


import org.hibernate.annotations.ColumnDefault;

import com.ysk.converter.RoleConverter;
import com.ysk.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "members")
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_seq")
  private Long memberSeq;     // 회원번호 

  @Column(name = "id", unique = true, nullable = false, length = 20)
  private String loginId;     // 아이디

  @Column(nullable = false, length = 200)
  private String password;    // 비밀번호

  @Column(nullable = false, length = 10)
  private String name;        // 이름

  @Column(length = 20)
  private String phone;       // 휴대폰번호

  @Column(length = 10)
  private String zipCode;     // 우편번호

  private String addr1;       // 기본주소
  private String addr2;       // 상세주소

  @Column(nullable = false)
  @ColumnDefault("false")
  private Boolean isDormant;  // 휴먼계정 여부

  @Convert(converter = RoleConverter.class)
  @Column(name = "role", length = 1)
  private Role role;          // 권한 정보 (Admin, User 등)

}
