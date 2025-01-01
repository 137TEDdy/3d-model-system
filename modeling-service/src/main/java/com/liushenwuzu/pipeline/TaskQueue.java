package com.liushenwuzu.pipeline;

/**
 * 任务队列.
 */
public interface TaskQueue {
  /**
   * 向队列加一个任务，任务是排队进行的,
   * 队列要是可持久化的、能互斥访问的,
   * 目前的思路是用redis实现,
   * 供生产者端调用.
   *
   * @param task 任务
   * @return 返回预估完成时间，队列满了就-1
   */
  int add(Object task);

  /**
   * 从队列取一个任务进行处理，优先取未ACK的，再取待做的,
   * 供消费者线程调用.
   *
   * @return 有就返回，无就null
   */
  Object pop();

  boolean haveSpareQueue();

}
