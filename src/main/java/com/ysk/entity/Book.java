package com.ysk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "book")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_seq")
    private Long bookSeq;           // 도서 고유 ID

    @Column(nullable = false, length = 200)
    private String title;           // 도서 제목

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;            // ISBN

    @Column(nullable = false, length = 100)
    private String author;          // 작가

    @Column(length = 100)
    private String publisher;       // 출판사

    @Column(length = 50)
    private String category;        // 카테고리

    @Lob 
    @Column(columnDefinition = "TEXT")
    private String description;     // 책 소개

    private String thumbnailUrl;    // 표지 이미지 URL

    private LocalDate publicationDate; // 출판일자

    @Column(nullable = false)
    private Integer totalQuantity;     // 전체 재고

    @Column(nullable = false)
    private Integer availableQuantity; // 대출 가능 수량

    // 도서 대출 시 호출 (재고 감소)
    public void decreaseStock() {
        if (this.availableQuantity > 0) {
            this.availableQuantity--;
        } else {
            throw new IllegalStateException("대출 가능한 재고가 없습니다.");
        }
    }

    // 도서 반납 시 호출 (재고 증가)
    public void increaseStock() {
        if (this.availableQuantity < this.totalQuantity) {
            this.availableQuantity++;
        }
    }
}