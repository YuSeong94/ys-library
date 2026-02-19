package com.ysk.repository.community;

import com.ysk.entity.community.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
    // 특정 게시글(boardSeq)에 달린 댓글을 등록일자(regDatetime) 오름차순으로 조회
    List<Reply> findByBoard_BoardSeqOrderByRegDatetimeAsc(Long boardSeq);
    
}