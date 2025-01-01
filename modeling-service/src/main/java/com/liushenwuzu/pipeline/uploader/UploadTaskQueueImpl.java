package com.liushenwuzu.pipeline.uploader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushenwuzu.pipeline.TaskQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 任务队列的实现.
 */
@Component
public class UploadTaskQueueImpl implements TaskQueue {

  @Autowired
  private StringRedisTemplate redis;

  /**
   * 队列限制数量.
   */
  @Value("${plyFactory.num}")
  private int plyLimitNum;
  @Value("${plyFactory.uploadDatabase}")
  private String redisDatabaseName;

  /**
   * 互斥悲观锁，防止redis冲突.
   */
  private Object changeable = new Object();

  private static final Logger logger = LoggerFactory.getLogger(UploadTaskQueueImpl.class);

  @Override
  public int add(Object task) {
    synchronized (changeable) {
      try {
        int num = redis.opsForList().size(redisDatabaseName).intValue();
        System.out.println("upload " + num);
        if (num == plyLimitNum) {
          return -1;
        }
        String json = new ObjectMapper().writeValueAsString(task);
        UploadTask uploadTask = (UploadTask) task;
        logger.info("加入Upload任务：" + uploadTask.getUserId() + " "
            + uploadTask.getModelId() + " " + uploadTask.getTimes());
        redis.opsForList().leftPush(redisDatabaseName, json);
        return (num + 1) * 6;
      } catch (Exception e) {
        logger.warn("模型服务器繁忙");
        return -1;
      }
    }
  }

  @Override
  public Object pop() {
    synchronized (changeable) {
      try {
        String val = redis.opsForList().rightPop(redisDatabaseName);
        UploadTask task = new ObjectMapper().readValue(val, UploadTask.class);
        logger.info("取出Upload任务：" + task.getUserId() + " "
            + task.getModelId() + " " + task.getTimes());
        return task;
      } catch (Exception e) {
        return null;
      }
    }
  }

  @Override
  public boolean haveSpareQueue() {
    return (redis.opsForList().size(redisDatabaseName).intValue() < plyLimitNum);
  }
}
