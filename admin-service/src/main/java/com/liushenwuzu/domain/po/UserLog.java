package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户登录日志.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_log")
@ApiModel(value = "UserLog对象", description = "用户登录日志")
public class UserLog implements Serializable {
  @TableId(value = "user_log_id", type = IdType.AUTO)
  private Integer userLogId;
  private String logTime;
  private Integer userId;
  private String ip;
  private Integer status;
}
