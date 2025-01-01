package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.mapper.CommentMapper;
import com.liushenwuzu.domain.po.Comment;
import com.liushenwuzu.domain.vo.CommentVo;
import com.liushenwuzu.service.ICommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements ICommentService {

  @Autowired
  private CommentMapper commentMapper;

  /**
   * @param momentId
   * @return {@link List}<{@link CommentVo}>
   */
  @Override
  public List<CommentVo> getCommentsUnderMoment(int momentId) {
    return commentMapper.selectCommentsUnderMoment(momentId);
  }
}
