package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 销售日志.
 */
@Data
@TableName("sales_log")
public class SalesLog {
  @TableId(value = "sale_log_id", type = IdType.AUTO)
  private int saleLogId;
  private int modelId;
  private int userId;
  private int targetUserId;
  private String saleCreatedTime;
  private int status;
  private int price;

  public SalesLog(){

  }
  public SalesLog(int modelId, int userId, int targetUserId, String saleCreatedTime, int status,
      int price) {
    this.modelId = modelId;
    this.userId = userId;
    this.targetUserId = targetUserId;
    this.saleCreatedTime = saleCreatedTime;
    this.status = status;
    this.price = price;
  }
}
