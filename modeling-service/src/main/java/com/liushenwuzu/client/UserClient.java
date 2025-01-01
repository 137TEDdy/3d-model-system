package com.liushenwuzu.client;

import com.liushenwuzu.domain.po.User;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service")
public interface UserClient {

  /**
   * 根据用户id查询用户接口.
   *
   * @param userId
   * @return
   */
  @GetMapping("/api/user/queryById")
  User queryUserById(@RequestParam("userId") Integer userId);

  /**
   * 增加此用户生成模型次数.
   *
   * @param userId
   */
  @GetMapping("/api/user/add-num")
  void addNum(@RequestParam Integer userId);
}
