package com.liushenwuzu.service;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.User;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 服务类
 */
public interface IUsersService extends IService<User> {

  /**
   * 查询某用户自己所有分享的模型接口
   *
   * @param userId
   * @return {@link Result}
   */
  Result ownPublished(Integer userId);
}
