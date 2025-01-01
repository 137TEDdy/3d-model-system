package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * 用户返回表单.
 */
@Data
public class UserVo {

  private String nickname;

  private String phone;

  private String password;


  private Integer userId;

  private String registerTime;

  private String avatarUrl;

  private String info;


  private Integer status;


  private Integer isVip;


  private Integer isMerchant;


  private int account;


  private Integer modelNum;
  private String token;//jwt令牌

}
