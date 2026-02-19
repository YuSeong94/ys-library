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
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq")
    private Long replySeq;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 댓글 내용

    /**
     * 게시글 (Board) 연관관계 설정
     * - 댓글(N) : 게시글(1)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", nullable = false)
    private Board board;

    /**
     * 작성자 (Member) 연관관계 설정
     * - 댓글(N) : 작성자(1)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    // 생성자 (서비스 계층에서 쉽게 만들기 위해)
    public Reply(String content, Board board, Member member) {
        this.content = content;
        this.board = board;
        this.member = member;
    }

    // 수정 메서드 (Dirty Checking용)
    public void update(String content) {
        this.content = content;
    }
}