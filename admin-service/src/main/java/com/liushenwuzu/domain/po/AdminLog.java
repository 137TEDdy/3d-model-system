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
 * 管理员登录日志.
 *
 * @author clk
 * @since 2023-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin_log")
@ApiModel(value = "AdminLog对象", description = "管理员登录日志")
public class AdminLog implements Serializable {
  @TableId(value = "admin_log_id", type = IdType.AUTO)
  private Integer adminLogId;
  private String logTime;
  private Integer adminId;
  private String ip;
  private Integer status;

}
