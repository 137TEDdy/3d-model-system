package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 登录表单实体.
 */
@Data
@ApiModel(description = "登录表单实体")
public class AdminLoginFormDto {
  private String phone;
  private String password;
  private String ip;
}
