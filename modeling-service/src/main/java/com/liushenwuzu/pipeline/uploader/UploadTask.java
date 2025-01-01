package com.liushenwuzu.pipeline.uploader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务实体.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadTask {
  private Integer userId;
  private String times;
  private String directoryPath;
  private Integer modelId;
}
