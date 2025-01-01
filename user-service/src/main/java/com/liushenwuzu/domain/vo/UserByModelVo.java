package com.liushenwuzu.domain.vo;

import lombok.Data;

/**
 * 模型查用户返回表单.
 */
@Data
public class UserByModelVo {

  private int modelId;
  private int userId;
  private int price;
  private String createdTime;
  private String coverUrl;
  private String filePath;
  private String modelName;


  /**
   * 购买模型表单实体
   *
   * @param modelId
   * @param userId
   * @param price
   * @param createdTime
   * @param coverUrl
   * @param filePath
   * @param modelName
   */
  public UserByModelVo( int modelId, int userId,
     int price, String createdTime,
      String coverUrl, String filePath,
      String modelName) {
    this.modelId = modelId;
    this.userId = userId;
    this.price = price;
    this.createdTime = createdTime;
    this.coverUrl = coverUrl;
    this.filePath = filePath;
    this.modelName = modelName;
  }

}
