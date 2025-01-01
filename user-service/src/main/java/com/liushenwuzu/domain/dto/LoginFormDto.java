package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

/**
 * 登录表单实体.
 */
@Data
@ApiModel(description = "登录表单实体")
public class LoginFormDto {
  @ApiModelProperty(value = "手机号码", required = true)
  private String phone;
  @ApiModelProperty(value = "密码", required = true)
  private String password;
  private String ip;
  private String code;
}
