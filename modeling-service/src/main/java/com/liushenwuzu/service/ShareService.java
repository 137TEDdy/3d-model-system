package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.domain.po.Share;


/**
 * 分享service接口
 */
public interface ShareService extends IService<Share> {

  /**
   * @param modelId
   * @return int
   */
  int isShared(int modelId);
}