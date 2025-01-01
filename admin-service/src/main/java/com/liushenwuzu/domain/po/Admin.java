package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 管理员表.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin")
@ApiModel(value = "Admin对象", description = "管理员表")
public class Admin implements Serializable {
  @TableId(value = "admin_Id", type = IdType.AUTO)
  private Integer adminId;
  private String nickname;
  private String password;
  private String phone;
  private String avatarUrl;
  private String email;
}
