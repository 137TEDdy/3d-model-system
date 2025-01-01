package com.liushenwuzu.pipeline.downloader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消费任务实体.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadTask {
  private String identification;
  private String localDirectory;
  private Integer modelId;
}
