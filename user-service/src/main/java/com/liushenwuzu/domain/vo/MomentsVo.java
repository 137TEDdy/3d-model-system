package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * 动态返回表单.
 */
@Data
public class MomentsVo {
  private int momentId;
  private int userId;
  private String content;
  private String createdTime;
  private String title;
  private String avatarUrl;
  private String nickname;
}
