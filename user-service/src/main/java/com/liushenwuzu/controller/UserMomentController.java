package com.liushenwuzu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Comment;
import com.liushenwuzu.domain.po.Moments;
import com.liushenwuzu.service.ICommentService;
import com.liushenwuzu.service.IMomentsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 动态的控制层.
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户动态接口")
@RequiredArgsConstructor
public class UserMomentController {

  private final IMomentsService momentsService;
  private final ICommentService commentService;

  /**
   * 发布动态.
   *
   * @param userId  用户ID
   * @param title   题目
   * @param content 内容
   * @return 执行信息
   */
  @GetMapping("/publish-moments")
  @ApiOperation("发布动态")
  public Result publishMoments(@RequestParam Integer userId, @RequestParam String title,
                               @RequestParam String content) {
    if (momentsService.save(new Moments(userId, title, content))) {
      return Result.success();
    }
    return Result.error("数据存储错误");
  }

  /**
   * 查询动态.
   *
   * @param userId 用户ID
   * @return 执行信息
   */
  @GetMapping("/query-moments")
  @ApiOperation("查询动态")
  public Result queryMoments(@RequestParam(required = false) Integer userId) {
    if (userId == null || userId == 0) {
      return Result.success(momentsService.listMomentsVO());
    }
    return Result.success(momentsService.getMomentsVO(userId));
  }

  /**
   * 删除自己的动态.
   *
   * @param userId   用户ID
   * @param momentId 动态ID
   * @return 执行信息
   */
  @GetMapping("/delete-moment")
  @ApiOperation("删除自己的动态")
  public Result deleteMoment(@RequestParam Integer userId,
                             @RequestParam(required = false) Integer momentId) {
    if (momentId == null || momentId == 0) { // 删自己的所有动态
      if (momentsService.remove(new QueryWrapper<Moments>().eq("user_id", userId))) {
        return Result.success();
      }
      return Result.error("删除错误");
    }
    if (momentsService.removeById(momentId)) {
      return Result.success();
    }
    return Result.error("删除错误");
  }

  /**
   * 发表留言.
   *
   * @param userId   用户ID
   * @param momentId 动态ID
   * @param content  内容
   * @return 执行结果
   */
  @GetMapping("/publish-comment")
  @ApiOperation("发表留言")
  public Result publishComment(@RequestParam int userId, @RequestParam int momentId,
                               @RequestParam String content) {
    if(momentsService.getById(momentId)==null){
      return Result.error("该动态不存在，无法留言");
    }
    if (commentService.save(new Comment(userId, momentId, content))) {
      return Result.success();
    }
    return Result.error("数据存储错误");
  }

  /**
   * 删除自己留言.
   *
   * @param userId    用户ID
   * @param commentId 评论ID
   * @return 执行结果
   */
  @GetMapping("/delete-comment")
  @ApiOperation("删除自己留言")
  public Result deleteComment(@RequestParam(required = false) int userId,
                              @RequestParam int commentId) {
    if (commentService.removeById(commentId)) {
      return Result.success();
    }
    return Result.error("删除错误");
  }

  /**
   * 查询某动态的留言列表.
   *
   * @param momentId 动态ID
   * @return 执行结果
   */
  @GetMapping("/comment-list")
  @ApiOperation("查询某动态的留言列表")
  public Result listComment(@RequestParam int momentId) {
    return Result.success(commentService.getCommentsUnderMoment(momentId));
  }

}
