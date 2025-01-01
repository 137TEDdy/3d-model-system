package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * 查需求返回表单.
 */
@Data
public class UserQueryNeedVo {
  private String nickname;

  private String avatarUrl;

  private Integer needId;

  private Integer userId;

  private String title;

  private String note;

  private String createdTime;

  private Integer isSatisfied;
}
