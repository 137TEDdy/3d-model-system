package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 认证审核.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("verification")
@ApiModel(value = "Verification对象", description = "认证审核")
public class Verification implements Serializable {

  @TableId(value = "verification_id", type = IdType.AUTO)
  private Integer verificationId;

  private Integer userId;

  private String note;

  private String createdTime;

  @ApiModelProperty(value = "0为在审核，1为通过，2为未通过")
  private Integer status;


}
