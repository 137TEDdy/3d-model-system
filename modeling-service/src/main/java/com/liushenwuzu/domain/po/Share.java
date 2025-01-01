package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 数据库分享实体
 */
@Data
@TableName("shares")
public class Share {

  @TableId(type = IdType.AUTO)
  private Integer shareId;
  private Integer modelId;
  private Integer userId;
  private String createdTime;
}
