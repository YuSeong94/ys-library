package com.ysk.dto.community;

import com.ysk.entity.community.Board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardWriteDto {
  
  private String title;
  private String content;

  public Board toEntity() {
    return Board.builder().title(this.title)
                          .content(this.content)
                          .viewCount(0)
                          .build();
  }
}
