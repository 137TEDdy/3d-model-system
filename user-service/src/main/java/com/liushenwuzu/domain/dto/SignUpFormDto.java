package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 用户注册表单实体.
 */
@Data
@ApiModel(description = "用户注册表单实体")
public class SignUpFormDto {
  private String phone;
  private String password;
  private String verifyCode;
}
