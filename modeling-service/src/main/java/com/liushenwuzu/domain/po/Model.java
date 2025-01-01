package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 数据库模型实体
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Data
@TableName("model")
public class Model {

  @TableId(type = IdType.AUTO)
  private Integer modelId;
  private Integer userId;
  private String info;
  private String name;
  private String filePath;
  private String coverUrl;
  private int modelSize;
  private int status;//0为成功，1为失败，2为进行中，3是被锁定,4是未生成
  private int isBought;//是否为购买得来的模型
  private String createdTime;
}
