package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 查询用户表单表单实体.
 */
@Data
@ApiModel(description = "查询用户表单表单实体")
public class QueryUserFormDto {
  private Integer userId;
  private String nickname;
  private String phone;

}
