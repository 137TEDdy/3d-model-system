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
 * 动态对象.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("moments")
@ApiModel(value = "Moments对象", description = "用户发布动态")
public class Moments {
  @TableId(type = IdType.AUTO)
  private int momentId;
  private int userId;
  private String content;
  private String createdTime;
  private String title;

  /**
   * 生成插入数据库的对象，自动生成时间.
   *
   * @param userId  用户ID
   * @param title   题目
   * @param content 内容
   */
  public Moments(int userId, String title, String content) {
    this.userId = userId;
    this.title = title;
    this.content = content;
    this.createdTime = LocalDateTime.now().toString();
  }
}
