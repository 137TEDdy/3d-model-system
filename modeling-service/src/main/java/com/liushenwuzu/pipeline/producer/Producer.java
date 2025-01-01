package com.liushenwuzu.pipeline.producer;

import com.jcraft.jsch.JSchException;
import com.liushenwuzu.pipeline.TaskQueue;
import com.liushenwuzu.pipeline.downloader.DownloadTask;
import com.liushenwuzu.pipeline.downloader.Downloader;
import com.liushenwuzu.pipeline.utils.ApplicationBeanFactory;
import com.liushenwuzu.pipeline.utils.JschUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 生成线程，执行生成任务.
 */
@Component
public class Producer extends Thread {

  private static final Logger logger = LoggerFactory.getLogger(Producer.class);
  private final TaskQueue taskQueue;

  /**
   * 服务器配置.
   */
  @Value("${plyFactory.address}")
  private String address;
  @Value("${plyFactory.port}")
  private int port;
  @Value("${plyFactory.username}")
  private String username;
  @Value("${plyFactory.password}")
  private String password;
  @Value("${plyFactory.dstDirectory}")
  private String dstDirectory;

  /**
   * 任务详情.
   */
  private Integer userId;
  private String times;
  private long focal;
  private String localDirectory;
  private Integer modelId;

  private JschUtil plyServer; //连接器

  public Producer(ProductTaskQueueImpl queue) {
    this.taskQueue = queue;
  }

  /**
   * 包装方法执行生成脚本.
   *
   * @throws Exception IO异常
   */
  private void remoteProduct(String command) throws JSchException, IOException {
    //连接
    if (plyServer == null) {
      plyServer = new JschUtil(address, port, username, password);
    }
    boolean connect = plyServer.connect();
    if (!connect) {
      throw new JSchException("模型服务器连接失败");
    }

    //执行
    logger.info("Producer执行生成脚本: " + command);
    plyServer.execSudoCommand(command);
  }

  private void init(ProductTask productTask) {
    userId = productTask.getUserId();
    times = productTask.getTimes();
    focal = productTask.getFocal();
    localDirectory = productTask.getLocalDirectory();
    modelId = productTask.getModelId();
  }

  @Override
  public void run() {
    ProductTask productTask;
    while (true) {
      productTask = (ProductTask) taskQueue.pop();
      if (productTask != null) {
        logger.info("执行Product任务：" + productTask.getUserId() + " " + productTask.getTimes());
        init(productTask);
        try {
          if (productTask.getFocal() == -1L) {
            remoteProduct("sh /home/zhujiada/mvgImages/recons_special.sh " + userId + ' ' + times);
          } else {
            remoteProduct("sh /home/zhujiada/mvgImages/recons.sh " + userId + ' '
                + times + ' ' + focal);
          }
          nextStage();
        } catch (JSchException e) {
          logger.error(e.getMessage());
          System.out.println("连接中断");
          e.printStackTrace();
        } catch (Exception e) {
          logger.error(e.getMessage());
        }
      } else {
        //关ply连接，归还资源
        if (plyServer != null) {
          plyServer.close();
          plyServer = null;
        }
        synchronized (waitObj) { //使用wait必须的同步
          try {
            logger.warn("Product队列中无任务，Producer进入WAITING状态");
            waitObj.wait();
            logger.info("Producer已被重新唤醒");
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private void nextStage() {
    TaskQueue taskQueue = (TaskQueue) ApplicationBeanFactory.getBean("downloadTaskQueueImpl");
    Downloader downloader = (Downloader) ApplicationBeanFactory.getBean("downloader");

    //加任务
    taskQueue.add(new DownloadTask(userId.toString() + "-" + times.toString(),
        localDirectory, modelId));

    //任务消费者没醒的话唤醒
    if (downloader.getState().equals(Thread.State.WAITING)) {
      downloader.wakeUp();
      System.out.println("Producer重新唤醒Downloader");
    }
  }

  private final Object waitObj = new Object();

  /**
   * 唤醒线程.
   */
  public void wakeUp() {
    synchronized (waitObj) {
      waitObj.notify();
    }
  }
}
