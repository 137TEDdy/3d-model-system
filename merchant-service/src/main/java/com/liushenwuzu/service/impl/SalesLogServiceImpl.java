package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.service.SalesLogService;
import com.liushenwuzu.domain.po.SalesLog;
import com.liushenwuzu.mapper.SalesLogMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易日志接口实现类
 */
@Slf4j
@Service
public class SalesLogServiceImpl extends ServiceImpl<SalesLogMapper, SalesLog> implements
    SalesLogService {

  @Autowired
  SalesLogMapper salesLogMapper;

  /**
   * 查看收入情况
   *
   * @param userId 商户id
   * @return 交易日志
   */
  @Override
  public List<SalesLog> cashFlowList(int userId) {
    QueryWrapper<SalesLog> queryWrapper = new QueryWrapper<SalesLog>()
        .eq("user_id", userId);
    return salesLogMapper.selectList(queryWrapper);
  }
}
