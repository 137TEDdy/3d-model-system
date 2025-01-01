package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.domain.po.Comment;
import com.liushenwuzu.domain.vo.CommentVo;

import java.util.List;

/**
 * 评论接口
 */
public interface ICommentService extends IService<Comment> {

  /**
   * 得到动态下评论接口
   *
   * @param momentId
   * @return {@link List}<{@link CommentVo}>
   */
  List<CommentVo> getCommentsUnderMoment(int momentId);
}
