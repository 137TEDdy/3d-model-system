package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * 商户登录表单数据
 */
@Data
public class MerchantVo {

  private String userId;//账号
  private String password;
  private int account;
  private String nickname;//昵称
  private String phone;//电话
  private String registerTime;//注册时间
  private String avatarUrl;//头像图片地址
  private String info;//个人简介
  private int status;//账户状态，0为正常，1为冻结
  private int isVip;//是否为vip，0为不是，1为是
  private int isMerchant;//是否为商户，0位不是，1为是
  private String token;//jwt令牌
}
