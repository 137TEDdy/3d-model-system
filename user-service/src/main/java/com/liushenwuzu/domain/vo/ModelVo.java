package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * 模型返回表单.
 */
@Data
public class ModelVo {
  private Integer modelId;
  private Integer userId;
  private String info;
  private String name;
  private String filePath;
  private String coverUrl;
  private int modelSize;
  private int status;//0为成功，1为失败，2为进行中，3是被锁定,4是未生成
  private String createdTime;
  private String nickname;
  private int isLiked;
}
