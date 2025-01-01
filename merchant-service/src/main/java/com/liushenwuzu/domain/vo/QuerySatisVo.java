package com.liushenwuzu.domain.vo;

import lombok.Data;


/**
 * 查询需求表单数据
 */
@Data
public class QuerySatisVo {

  private Integer satisfactionId; // 必需
  private Integer needId; // 必需
  private Integer userId; // 商户id，必需
  private Integer modelId; // 模型id，必需
  private String coverUrl; // 模型封面路径，必需
  private String modelName; // 模型名称，必需
  private String createdTime; // 必需
  private int price;
  private Integer isChosen; // 必需
}
