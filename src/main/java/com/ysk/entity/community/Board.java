package com.ysk.entity.community; 

import com.ysk.entity.BaseEntity;
import com.ysk.entity.Member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter 
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_seq")
    private Long boardSeq;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob // 대용량 텍스트 저장
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "view_count")
    private int viewCount = 0;

    /**
     * 작성자 (Member)와 연관관계 설정
     * - 게시글(N) : 작성자(1)
     * - FetchType.LAZY : 성능 최적화를 위해 작성자 정보는 필요할 때만 가져옴
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq") // DB 컬럼명
    private Member memberSeq;

    // 생성자 (서비스 계층에서 쉽게 만들기 위해)
    public Board(String title, String content, Member memberSeq) {
        this.title = title;
        this.content = content;
        this.memberSeq = memberSeq;
    }

    // 수정 메서드 (Dirty Checking용)
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}