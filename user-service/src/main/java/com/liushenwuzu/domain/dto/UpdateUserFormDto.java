package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiOperation;

import lombok.Data;

/**
 * 用户修改个人信息表单.
 */
@Data
@ApiOperation("用户修改个人信息表单")
public class UpdateUserFormDto {
  private Integer userId;
  private String info;
  private String nickname;

}
