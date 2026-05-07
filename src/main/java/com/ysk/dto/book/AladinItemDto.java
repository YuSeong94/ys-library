package com.ysk.dto.book;

import com.ysk.entity.book.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AladinItemDto {
    private String title;       // 책 제목
    private String author;      // 저자
    private String pubDate;     // 출판일 (예: 2023-10-25)
    private String description; // 책 소개
    private String isbn13;      // ISBN (알라딘은 isbn과 isbn13 두 개를 주는데, 보통 13자리를 씁니다)
    private String cover;       // 표지 이미지 URL
    private String publisher;   // 출판사
    private String categoryName;// 카테고리

    // 🔥 보너스: 이 DTO를 우리 DB에 저장할 Book 엔티티로 변환해주는 꿀 메서드!
    public Book toEntity() {
        return Book.builder()
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .isbn(this.isbn13)
                .description(this.description)
                .coverUrl(this.cover)
                .category(this.categoryName)
                // 알라딘 데이터에는 초기 재고가 없으니 기본값(예: 5권)을 줍니다.
                .totalQuantity(5) 
                .availableQuantity(5)
                .build();
    }
}