package com.liushenwuzu.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于封装所有返回给前端的结果
 *
 * @author 陈立坤
 * @date 2023/09/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

  private Integer code;//响应码，0 代表成功; 1 代表失败
  private String msg;  //响应信息 描述字符串
  private Object data; //返回的数据


  /**
   * @return {@link Result}
   */
  public static Result success() {
    return new Result(0, "success", null);
  }


  /**
   * @param data
   * @return {@link Result}
   */
  public static Result success(Object data) {
    return new Result(0, "success", data);
  }


  /**
   * @param msg
   * @return {@link Result}
   */
  public static Result error(String msg) {
    return new Result(1, msg, null);
  }

}
