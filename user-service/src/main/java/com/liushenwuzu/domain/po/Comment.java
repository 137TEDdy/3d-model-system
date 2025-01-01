package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论对象.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
@ApiModel(value = "Comment对象", description = "用户发布评论")
public class Comment {
  @TableId(type = IdType.AUTO)
  private int commentId;
  private int userId;
  private int momentId;
  private String content;
  private String createdTime;

  /**
   * 生成插入数据库的对象，自动生成时间.
   *
   * @param userId   用户ID
   * @param momentId 动态ID
   * @param content  内容
   */
  public Comment(int userId, int momentId, String content) {
    this.userId = userId;
    this.momentId = momentId;
    this.content = content;
    this.createdTime = LocalDateTime.now().toString();
  }
}
