package com.liushenwuzu.pipeline.uploader;

import com.jcraft.jsch.JSchException;
import com.liushenwuzu.pipeline.TaskQueue;
import com.liushenwuzu.pipeline.producer.Producer;
import com.liushenwuzu.pipeline.producer.ProductTask;
import com.liushenwuzu.pipeline.utils.ApplicationBeanFactory;
import com.liushenwuzu.pipeline.utils.JschUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 上传线程.
 */
@Component
public class Uploader extends Thread {

  private static final Logger logger = LoggerFactory.getLogger(Uploader.class);
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
  private String localDirectory;
  private Integer modelId;

  private JschUtil plyServer; //连接器

  public Uploader(UploadTaskQueueImpl queue) {
    this.taskQueue = queue;
  }

  /**
   * 包装方法建目录.
   *
   * @throws Exception IO异常
   */
  private void remoteMkdir(String remoteDirectory) throws JSchException, IOException {
    logger.info("exec：mkdir " + remoteDirectory);
    plyServer.execCommand("mkdir " + remoteDirectory);
  }

  /**
   * 包装方法上传.
   *
   * @param from 源
   * @param to   目的
   * @throws Exception IO异常
   */
  private void remoteUpload(String from, String to) throws JSchException, IOException {
    logger.info("上传" + from);
    plyServer.upload(from, to);
  }

  /**
   * 处理上传任务.
   */
  private void handleTask() throws JSchException, IOException {
    //连接
    if (plyServer == null) {
      plyServer = new JschUtil(address, port, username, password);
    }
    boolean connect = plyServer.connect();
    if (!connect) {
      throw new JSchException("模型服务器连接失败");
    }

    //检测本地文件夹
    File directory = new File(localDirectory);
    if (!directory.exists() || directory.list() == null) {
      throw new IOException("本地文件不存在");
    }

    //远端生成照片文件夹
    String remoteDirectory = dstDirectory + "imgs-" + userId.toString() + '-' + times;
    remoteMkdir(remoteDirectory);

    //上传照片组
    remoteDirectory += '/';
    for (String name : directory.list()) {
      remoteUpload(new File(localDirectory, name).getAbsolutePath(), remoteDirectory + name);
    }
  }

  @Override
  public void run() {
    UploadTask uploadTask;
    while (true) {
      uploadTask = (UploadTask) taskQueue.pop();
      if (uploadTask != null) {
        logger.info("执行Upload任务：" + +uploadTask.getUserId() + " "
            + uploadTask.getModelId() + " " + uploadTask.getTimes());
        userId = uploadTask.getUserId();
        times = uploadTask.getTimes();
        localDirectory = uploadTask.getDirectoryPath();
        modelId = uploadTask.getModelId();
        try {
          handleTask();
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
            logger.warn("Upload队列中无任务，Uploader进入WAITING状态");
            waitObj.wait();
            logger.info("Uploader已被重新唤醒");
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * 对内，获取照片焦距.
   *
   * @return 焦距
   * @throws IOException 文件异常
   */
  private long getFocal() {
    File oneImage = new File(localDirectory).listFiles()[0];
    try {
      BufferedImage bufferedImage = ImageIO.read(new FileInputStream(oneImage));
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();
      return Math.round(Math.max(height, width) * 1.2);
    } catch (IOException e) {
      return 0;
    }
  }

  private void nextStage() {
    TaskQueue taskQueue = (TaskQueue) ApplicationBeanFactory.getBean("productTaskQueueImpl");
    Producer producer = (Producer) ApplicationBeanFactory.getBean("producer");

    //加任务
    taskQueue.add(new ProductTask(userId, times, getFocal(), localDirectory, modelId));

    //任务消费者没醒的话唤醒
    if (producer.getState().equals(Thread.State.WAITING)) {
      producer.wakeUp();
      System.out.println("uploader重新唤醒producer");
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
