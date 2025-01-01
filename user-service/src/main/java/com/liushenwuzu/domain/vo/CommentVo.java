package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * 返回表单.
 */
@Data
public class CommentVo {
  private int commentId;
  private int userId;  // 评论者
  private int momentId;
  private String content;
  private String createdTime;
  private String avatarUrl;
  private String nickname;
}
