package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 数据库喜欢列表实体
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Data
@TableName("likes")
public class Like {

  @TableId(type = IdType.AUTO)
  private Integer likeId;
  private Integer userId;
  private Integer modelId;
  private String createdTime;
}
