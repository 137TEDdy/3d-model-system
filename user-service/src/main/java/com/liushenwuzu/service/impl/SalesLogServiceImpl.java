package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.mapper.SalesLogMapper;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.domain.po.SalesLog;
import com.liushenwuzu.domain.vo.UserByModelVo;
import com.liushenwuzu.mapper.ModelMapper;
import com.liushenwuzu.mapper.OnShowMapper;
import com.liushenwuzu.service.SalesLogService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 出售日志service层
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Slf4j
@Service
public class SalesLogServiceImpl extends ServiceImpl<SalesLogMapper, SalesLog> implements
    SalesLogService {

  @Autowired
  SalesLogMapper salesLogMapper;
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  OnShowMapper onShowMapper;

  /**
   * 查询用户购买的模型
   *
   * @param userId
   * @return {@link List}<{@link UserByModelVo}>
   */
  @Override
  public List<UserByModelVo> queryUserBought(int userId) {
    QueryWrapper<SalesLog> queryWrapper = new QueryWrapper<SalesLog>()
        .select("*")
        .eq("target_user_id", userId);
    List<SalesLog> salesLogs = salesLogMapper.selectList(queryWrapper);
    if (salesLogs.isEmpty()) {
      return null;
    }
    List<UserByModelVo> userByModelVos = new ArrayList<>();
    for (SalesLog salesLog : salesLogs) {
      Model model = modelMapper.selectById(salesLog.getModelId());
      userByModelVos.add(
          new UserByModelVo(salesLog.getModelId(), salesLog.getUserId(),
              salesLog.getPrice(), salesLog.getSaleCreatedTime(),
              model.getCoverUrl(), model.getFilePath(), model.getName()));
    }
    return userByModelVos;
  }

  /**
   * 保存出售日志
   *
   * @param salesLog
   */
  @Override
  public void saveSaleLog(SalesLog salesLog) {

    save(salesLog);
  }
}
