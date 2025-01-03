package com.liushenwuzu.domain.vo;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 管理员登录表单
 */
@Data
@ApiModel("管理员登录VO")
public class AdminVo {

  private Integer adminId;
  private String nickname;
  private String phone;
  private String avatarUrl;
  private String email;
  private String token;
}
