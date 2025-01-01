package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 更改密码表单.
 */
@Data
@ApiModel(description = "更改密码表单")
public class ChangePasswordFormDto {
  private String oldPassword;
  private String newPassword;
  private String phone;
}
