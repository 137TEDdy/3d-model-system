package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.Comment;
import com.liushenwuzu.domain.vo.CommentVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论映射层.
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

  /**
   * @param momentId
   * @return {@link List}<{@link CommentVo}>
   */
  List<CommentVo> selectCommentsUnderMoment(@Param("momentId") int momentId);
}
