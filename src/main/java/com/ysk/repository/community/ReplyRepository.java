package com.ysk.repository.community;

import com.ysk.entity.community.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
  List<Reply> findByBoard_BoardSeqOrderByRegDatetimeAsc(Long boardSeq);
  
  // 게시글 번호로 게시글의 댓글 수 조회
  int countByBoard_BoardSeq(Long boardSeq);

}