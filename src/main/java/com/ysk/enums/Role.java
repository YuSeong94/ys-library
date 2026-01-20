package com.ysk.enums;

import lombok.Getter;

@Getter
public enum Role {
  
  ADMIN('A', "관리자"),
  USER('U', "일반사용자");

  private final char code;
  private final String description;

  Role(char code, String description){
    this.code = code;
    this.description = description;
  }

  // DB의 char 값을 Role 타입으로 변환하기 위한 static 메서드
  public static Role fromCode(char code){
    for (Role r : Role.values()){
      if(r.getCode() == code){
        return r;
      }
    }
    throw new IllegalArgumentException("Invaild Role Code: " + code);
  }


}
