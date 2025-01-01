package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.domain.po.Satisfaction;
import com.liushenwuzu.domain.vo.QuerySatisVo;

import java.util.List;

/**
 * 满足service层
 *
 * @author 陈立坤
 * @date 2023/12/20
 */
public interface SatisfactionService extends IService<Satisfaction> {

  /**
   * 查询满足
   *
   * @param needId
   * @return {@link List}<{@link QuerySatisVo}>
   */
  List<QuerySatisVo> querySatis(int needId);

  /**
   * 挑选满足
   *
   * @param userId
   * @param satisfactionId
   * @param choice
   * @return {@link Integer}
   */
  Integer selectSatis(int userId, int satisfactionId, int choice);

}
