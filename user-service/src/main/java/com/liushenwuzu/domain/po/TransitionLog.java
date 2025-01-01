package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 个人交易日志
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("transition_log")
@ApiModel(value = "TransitionLog对象", description = "个人交易日志")
public class TransitionLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "transition_log_id", type = IdType.AUTO)
  private Integer transitionLogId;

  private Integer userId;

  private String createdTime;

  private Integer status;

  private int amount;

  private Integer type;


}
