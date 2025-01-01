package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 查询模型表单
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Data
@ApiModel(description = "查询模型表单")
public class SearchModelDto {

  private Integer userId;
  private Integer modelId;
  private String keywords;
}
