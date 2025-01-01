package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 商品交易信息实例
 */
@Data
@TableName("sales_log")
public class SalesLog {

  @TableId(type = IdType.AUTO)
  private int saleLogId;
  private int modelId;
  private int userId;
  private int targetUserId;
  private String saleCreatedTime;
  private int status;
  private int price;
}
