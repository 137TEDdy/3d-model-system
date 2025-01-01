package com.liushenwuzu.service;

import com.liushenwuzu.domain.vo.UserQueryNeedVo;
import com.liushenwuzu.domain.po.Needs;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户发布的3d需求 服务类
 *
 * @author clk
 * @since 2023-11-12
 */
public interface INeedsService extends IService<Needs> {

  /**
   * 查询需求
   *
   * @param userId
   * @return {@link List}<{@link UserQueryNeedVo}>
   */
  List<UserQueryNeedVo> queryNeeds(int userId);

  /**
   * 更改需求
   *
   * @param needId
   * @param note
   * @param title
   * @return boolean
   */
  boolean modifyNeeds(int needId, String note, String title);
}
