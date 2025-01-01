package com.liushenwuzu.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审核表单VO
 *
 * @author 陈立坤
 * @date 2023/12/20
 */
@Data
@ApiModel("审核表单VO")
public class VerificationVo {

  private Integer verificationId;
  private String note;
  private LocalDateTime createdTime;
  @ApiModelProperty(value = "0为在审核，1为通过，2为未通过")
  private Integer status;
}
