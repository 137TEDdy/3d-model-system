package com.liushenwuzu.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 在售视图返回表单.
 */
@Data
@TableName("on_show")
public class ViewOnShowVo {
  private int onSaleId;
  private int modelId;
  private int userId;
  private String note;
  private int num;
  private int price;
  private String createdTime;
  private String modelName;
  private String coverUrl;
  private String filePath;

}
