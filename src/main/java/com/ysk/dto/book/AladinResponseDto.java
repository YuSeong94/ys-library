package com.ysk.dto.book;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AladinResponseDto {
    // 알라딘 API는 검색된 책 목록을 "item"이라는 키값의 배열로 내려줍니다.
    private List<AladinItemDto> item; 
}