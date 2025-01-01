package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 数据库图片实体
 */
@Data
@TableName("images")
public class Image {

  @TableId(type = IdType.AUTO)
  private Integer imageId;
  private String imagePath;
  private String uploadTime;
  private Integer modelId;
  private int status;
}
