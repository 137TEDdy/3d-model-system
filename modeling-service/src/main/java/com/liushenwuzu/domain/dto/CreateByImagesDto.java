package com.liushenwuzu.domain.dto;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * 用图片生成模型表单
 */
@Data
@ApiModel(description = "用图片生成模型表单")
public class CreateByImagesDto {
  private String name;
  private String info;
  private Integer userId;
  private int num;

}
