package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 需求表实例
 */
@Data
@TableName("satisfaction")
public class Satisfaction {

  @TableId(type = IdType.AUTO)
  private int satisfactionId;
  private int needId;
  private int userId;
  private String modelUrl;
  private String createdTime;
  private int isChosen;
  private int price;
  private int onSaleId;
}
