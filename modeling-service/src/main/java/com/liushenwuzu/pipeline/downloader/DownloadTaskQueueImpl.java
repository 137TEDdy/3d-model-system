package com.liushenwuzu.pipeline.downloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushenwuzu.pipeline.TaskQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 下载任务队列实现.
 */
@Component
public class DownloadTaskQueueImpl implements TaskQueue {

  @Autowired
  private StringRedisTemplate redis;

  @Value("${plyFactory.downloadDatabase}")
  private String redisDatabaseName;

  private Object changeable = new Object();

  private static final Logger logger = LoggerFactory.getLogger(DownloadTaskQueueImpl.class);

  @Override
  public int add(Object task) {
    synchronized (changeable) {
      try {
        String json = new ObjectMapper().writeValueAsString(task);
        DownloadTask downloadTask = (DownloadTask) task;
        logger.info("加入Download任务：" + downloadTask.getIdentification());
        redis.opsForList().leftPush(redisDatabaseName, json);
      } catch (Exception e) {
        logger.warn("模型服务器繁忙");
      }
      return 0;
    }
  }

  @Override
  public Object pop() {
    synchronized (changeable) {
      try {
        String val = redis.opsForList().rightPop(redisDatabaseName);
        DownloadTask task = new ObjectMapper().readValue(val, DownloadTask.class);
        logger.info("取出Download任务：" + task.getIdentification());
        return task;
      } catch (Exception e) {
        return null;
      }
    }
  }

  @Override
  public boolean haveSpareQueue() {
    return false;
  }
}
