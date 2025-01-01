package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户发布的3d需求.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("needs")
@ApiModel(value = "Needs对象", description = "用户发布的3d需求")
public class Needs implements Serializable {

  @TableId(value = "need_id", type = IdType.AUTO)
  private Integer needId;

  @TableField("user_id")
  private Integer userId;

  @TableField("title")
  private String title;

  @TableField("note")
  private String note;

  @TableField("created_time")
  private String createdTime;

  @ApiModelProperty(value = "0为未满足，1为满足")
  @TableField("is_satisfied")
  private Integer isSatisfied;

}
