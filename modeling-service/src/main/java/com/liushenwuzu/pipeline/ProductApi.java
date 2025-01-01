package com.liushenwuzu.pipeline;

import com.liushenwuzu.pipeline.producer.Producer;
import com.liushenwuzu.pipeline.producer.ProductTask;
import com.liushenwuzu.pipeline.uploader.UploadTask;
import com.liushenwuzu.pipeline.uploader.UploadTaskQueueImpl;
import com.liushenwuzu.pipeline.uploader.Uploader;
import com.liushenwuzu.pipeline.utils.ApplicationBeanFactory;

/**
 * 相当于Api适配器，因为之前有一版实现方法，但不完整，后面改了方法的实现，但外部调用还是基本不变.
 */
public class ProductApi {

  /**
   * 请求生成，非阻塞，对外，相当于通知一下，初始的status2要自己set.
   *
   * @param userId        请求生成模型的用户Id
   * @param times         是该用户生成的第几次，如果数据库中没记录次数的字段的话用生成时间也行，反正同个用户标识不同模型即可
   * @param directoryPath 照片文件的文件夹路径
   */
  public static int noteToProduct(Integer userId, String times, String directoryPath,
                                  Integer modelId) {

    TaskQueue taskQueue = (TaskQueue) ApplicationBeanFactory.getBean("uploadTaskQueueImpl");
    Uploader uploader = (Uploader) ApplicationBeanFactory.getBean("uploader");

    //加任务
    int predictTime = taskQueue.add(new UploadTask(userId, times, directoryPath, modelId));

    if (predictTime == -1) {
      return -1;
    }

    //任务消费者没醒的话唤醒
    if (uploader.getState().equals(Thread.State.WAITING)) {
      uploader.wakeUp();
      System.out.println("mainThread重新唤醒uploader");
    }
    return predictTime;
  }

  /**
   * 特殊生成.
   *
   * @param userId        请求生成模型的用户Id
   * @param times         是该用户生成的第几次，如果数据库中没记录次数的字段的话用生成时间也行，反正同个用户标识不同模型即可
   * @param directoryPath 照片文件的文件夹路径
   * @param modelId       模型ID
   */
  public static void noteToProductSpecially(Integer userId, String times, String directoryPath,
                                            Integer modelId) {

    TaskQueue taskQueue = (TaskQueue) ApplicationBeanFactory.getBean("productTaskQueueImpl");
    Producer producer = (Producer) ApplicationBeanFactory.getBean("producer");

    //加任务
    taskQueue.add(new ProductTask(userId, times, -1L, directoryPath, modelId));

    //任务消费者没醒的话唤醒
    if (producer.getState().equals(Thread.State.WAITING)) {
      producer.wakeUp();
      System.out.println("mainThread重新唤醒producer");
    }
  }

  /**
   * 任务队列是否空余.
   *
   * @return 真假
   */
  public static boolean haveSpareQueue() {
    TaskQueue taskQueue = (UploadTaskQueueImpl) ApplicationBeanFactory
        .getBean("uploadTaskQueueImpl");
    return taskQueue.haveSpareQueue();
  }
}
