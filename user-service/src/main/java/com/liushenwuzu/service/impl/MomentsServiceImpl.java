package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.domain.po.Moments;
import com.liushenwuzu.domain.vo.MomentsVo;
import com.liushenwuzu.mapper.MomentsMapper;
import com.liushenwuzu.service.IMomentsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author totoro
 * @date 2023/12/19
 */
@Service
public class MomentsServiceImpl extends ServiceImpl<MomentsMapper, Moments>
    implements IMomentsService {

  @Autowired
  private MomentsMapper momentsMapper;

  /**
   * 查看评论接口
   *
   * @return {@link List}<{@link MomentsVo}>
   */
  @Override
  public List<MomentsVo> listMomentsVO() {
    return momentsMapper.selectMomentsVO();
  }

  /**
   * 查看评论接口
   *
   * @param userId
   * @return {@link List}<{@link MomentsVo}>
   */
  @Override
  public List<MomentsVo> getMomentsVO(Integer userId) {
    return momentsMapper.selectMomentsVOByUserId(userId);
  }
}
