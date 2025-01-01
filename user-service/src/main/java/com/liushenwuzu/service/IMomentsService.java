package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.domain.po.Moments;
import com.liushenwuzu.domain.vo.MomentsVo;

import java.util.List;

/**
 * 动态service接口
 *
 * @author 陈立坤
 * @date 2023/12/20
 */
public interface IMomentsService extends IService<Moments> {

  /**
   * 查询动态接口
   *
   * @return {@link List}<{@link MomentsVo}>
   */
  List<MomentsVo> listMomentsVO();

  /**
   * 得到动态接口
   *
   * @param userId
   * @return {@link List}<{@link MomentsVo}>
   */
  List<MomentsVo> getMomentsVO(Integer userId);
}
