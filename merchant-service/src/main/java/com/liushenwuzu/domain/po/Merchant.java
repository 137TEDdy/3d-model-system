package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 商户实体类
 */
@TableName("users")
@Data
public class Merchant {

  @TableId(type = IdType.AUTO)
  private String userId;//账号
  private String nickname;//昵称
  private String password;//密码
  private String email;//邮箱
  private String phone;//电话
  private String registerTime;//注册时间
  private String avatarUrl;//头像图片地址
  private String info;//个人简介
  private int status;//账户状态，0为正常，1为冻结
  private int isVip;//是否为vip，0为不是，1为是
  private int account;//账户余额
  private int isMerchant;//是否为商户，0位不是，1为是
}
