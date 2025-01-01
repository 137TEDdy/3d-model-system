package com.liushenwuzu.domain.vo;

import lombok.Data;


@Data
public class TransitionLogVo {

  private Integer transitionId;

  private Integer userId;

  private String createdTime;

  private Integer status;

  private int amount;

  private Integer type;

}
