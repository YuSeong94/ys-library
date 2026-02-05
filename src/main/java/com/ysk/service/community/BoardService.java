package com.ysk.service.community;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysk.dto.community.BoardResponseDto;
import com.ysk.entity.community.Board;
import com.ysk.repository.community.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;


  public List<BoardResponseDto> getBoardListAll() {
                
        List<Board> boards = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardSeq"));
        // Entity 리스트 -> DTO 리스트 변환
        return boards.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }


}
