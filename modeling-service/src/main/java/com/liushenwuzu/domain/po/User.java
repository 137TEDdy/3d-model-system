package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 数据库用户实体
 */
@Data
@TableName("users")
public class User {

  private String nickname;
  private String phone;
  private String password;
  @TableId(value = "user_id", type = IdType.AUTO)
  private Integer userId;
  private String registerTime;
  private String avatarUrl;
  private String info;
  @ApiModelProperty(value = "0正常，1为被锁定")
  private Integer status;
  @ApiModelProperty(value = "是否为VIP，0为不是，1为是")
  private Integer isVip;
  @ApiModelProperty(value = "是否为商户，0为不是，1为是")
  private Integer isMerchant;
  @ApiModelProperty(value = "余额")
  private int account;
  @ApiModelProperty(value = "生成次数")
  private Integer modelNum;
}
