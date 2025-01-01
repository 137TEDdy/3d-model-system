package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 需求实例
 */
@Data
@TableName("needs")
public class Need {

  @TableId(value = "need_id", type = IdType.AUTO)
  private int needId;
  private int userId;
  private String title;
  private String note;
  private String createdTime;
  private int isSatisfied;
}
