package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.domain.po.SalesLog;
import com.liushenwuzu.domain.vo.UserByModelVo;

import java.util.List;

/**
 * 售卖日志service层
 */
public interface SalesLogService extends IService<SalesLog> {

  /**
   * 查询用户购买的模型
   *
   * @param userId
   * @return {@link List}<{@link UserByModelVo}>
   */
  List<UserByModelVo> queryUserBought(int userId);

  /**
   * 保存交易日志
   *
   * @param salesLog
   */
  void saveSaleLog(SalesLog salesLog);

}
