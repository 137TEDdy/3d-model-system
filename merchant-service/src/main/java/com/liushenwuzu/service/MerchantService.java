package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Merchant;

/**
 * 商户接口
 */
public interface MerchantService extends IService<Merchant> {

  /**
   * 商户登录
   *
   * @param phone    电话号码
   * @param password 密码
   * @return 商户登录表单
   */
  Result login(String phone, String password);
}
