package com.ysk.dto.community;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyModifyRequestDto {
  private Long replySeq;
  private String content;
}