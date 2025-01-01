package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 充值表单实体.
 */
@Data
@ApiModel(description = "充值表单实体")
public class ChargeAccountForm {
  private Integer userId;
  private int num;
}
