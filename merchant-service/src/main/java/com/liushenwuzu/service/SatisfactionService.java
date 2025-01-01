package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Satisfaction;
import com.liushenwuzu.domain.vo.QuerySatisVo;

import java.util.List;

/**
 * 满足接口类
 */
public interface SatisfactionService extends IService<Satisfaction> {

  /**
   * 满足需求
   *
   * @param userId   商户id
   * @param needId   需求id
   * @param onshowId 商品id
   * @return 成功/失败
   */
  Result addSatis(int userId, int needId, int onshowId);

  /**
   * 查看商户满足的需求
   *
   * @param userId 商户id
   * @return 满足列表
   */
  List<QuerySatisVo> querySatis(int userId);
}
