package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.domain.po.SalesLog;

import java.util.List;

/**
 * 交易日志接口类
 */
public interface SalesLogService extends IService<SalesLog> {

  /**
   * 查看收入情况
   *
   * @param userId 商户id
   * @return 交易日志
   */
  List<SalesLog> cashFlowList(int userId);

}
