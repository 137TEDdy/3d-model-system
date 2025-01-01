package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 收藏列表.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("likes")
@ApiModel(value = "Likes对象", description = "收藏列表")
public class Likes implements Serializable {

  @TableId(value = "like_id", type = IdType.AUTO)
  private Integer likeId;

  private Integer userId;

  private Integer modelId;

  private String createdTime;

}
