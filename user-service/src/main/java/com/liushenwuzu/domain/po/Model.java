package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 模型.
 */
@Data
@TableName("model")
public class Model {
  @TableId(value = "model_id", type = IdType.AUTO)
  private Integer modelId;
  private Integer userId;
  private String info;
  private String name;
  private String filePath;
  private String coverUrl;
  private int modelSize;
  private int status;
  private int isBought;
  private String createdTime;
}
