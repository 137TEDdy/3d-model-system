package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.mapper.ShareMapper;
import com.liushenwuzu.service.ShareService;
import com.liushenwuzu.domain.po.Share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分享service层实现
 */
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements ShareService {

  @Autowired
  private ShareMapper shareMapper;

  /**
   * 判断模型是否已经分享了
   *
   * @param modelId
   * @return int
   */
  @Override
  public int isShared(int modelId) {
    QueryWrapper<Share> queryWrapper = new QueryWrapper<Share>()
        .select("*")
        .eq("model_id", modelId);
    Share share = shareMapper.selectOne(queryWrapper);
    if (share == null) {
      return 0;
    }
    return 1;
  }
}
