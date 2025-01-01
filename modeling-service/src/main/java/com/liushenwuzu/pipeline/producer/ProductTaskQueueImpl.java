package com.liushenwuzu.pipeline.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liushenwuzu.pipeline.TaskQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 生成任务队列的实现，所用redis的key不同，返回信息不同.
 */
@Component
public class ProductTaskQueueImpl implements TaskQueue {

  @Autowired
  private StringRedisTemplate redis;

  @Value("${plyFactory.productDatabase}")
  private String redisDatabaseName;

  /**
   * 互斥悲观锁.
   */
  private Object changeable = new Object();

  private static final Logger logger = LoggerFactory.getLogger(ProductTaskQueueImpl.class);

  @Override
  public int add(Object task) {
    synchronized (changeable) {
      try {
        String json = new ObjectMapper().writeValueAsString(task);
        ProductTask productTask = (ProductTask) task;
        logger.info("加入Product任务：" + productTask.getUserId() + " " + productTask.getTimes());
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
        ProductTask task = new ObjectMapper().readValue(val, ProductTask.class);
        logger.info("取出Product任务：" + task.getUserId() + " " + task.getTimes());
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
