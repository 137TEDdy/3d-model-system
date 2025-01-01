package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * @author 陈立坤
 * @date 2023/12/20
 */
@Data
public class TransitionLogVo {

  private Integer transitionId;

  private Integer userId;

  private String createdTime;

  private Integer status;

  private int amount;

  private Integer type;

}
