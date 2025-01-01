package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.Moments;
import com.liushenwuzu.domain.vo.MomentsVo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 动态映射层.
 */
@Mapper
public interface MomentsMapper extends BaseMapper<Moments> {

  /**
   * @return {@link List}<{@link MomentsVo}>
   */
  List<MomentsVo> selectMomentsVO();

  /**
   * @param userId
   * @return {@link List}<{@link MomentsVo}>
   */
  List<MomentsVo> selectMomentsVOByUserId(@Param("userId") Integer userId);
}
