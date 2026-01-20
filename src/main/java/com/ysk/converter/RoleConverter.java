package com.ysk.converter;

import com.ysk.enums.Role;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Character> {

  // Role Enum을 DB에 저장할 char 값으로 변환
  @Override
  public Character convertToDatabaseColumn(Role role){
    if(role == null){
      return null;
    } 
    return role.getCode();
  }

  // DB의 char 값을 Role Enum으로 변환
  @Override
  public Role convertToEntityAttribute(Character code){
    if(code == null){
      return null;
    }
    return Role.fromCode(code);
  }
}
