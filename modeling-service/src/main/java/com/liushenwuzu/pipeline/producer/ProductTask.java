package com.liushenwuzu.pipeline.producer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消费任务实体.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTask {
  private Integer userId;
  private String times;
  private long focal;
  private String localDirectory;
  private Integer modelId;
}
