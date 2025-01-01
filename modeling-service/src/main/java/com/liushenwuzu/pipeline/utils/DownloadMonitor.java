package com.liushenwuzu.pipeline.utils;

import com.jcraft.jsch.SftpProgressMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 下载文件的监督器.
 */
public class DownloadMonitor implements SftpProgressMonitor, Runnable {
  private static final Logger logger = LoggerFactory.getLogger(DownloadMonitor.class);
  private long download = 0; //目前已下载
  long startTime = 0L;

  private boolean isScheduled = false; // 标识有无建线程输出

  private ScheduledExecutorService executorService; //在给定的延迟之后运行任务的系统服务

  /**
   * 重载SftpProgressMonitor，文件开始传输时，调用初始化.
   *
   * @param op   选项，这里不需要特殊定义
   * @param src  源文件
   * @param dest 目的地址
   * @param max  最大文件，也不需要
   */
  @Override
  public void init(int op, String src, String dest, long max) {
    logger.info("开始下载文件：" + src);
    startTime = System.currentTimeMillis();
  }

  /**
   * 当每次传输了一个数据块后，调用count方法，
   * 也因此比较频繁，所以单独创建一个线程定时查询一下上传进度.
   *
   * @param count 本次传输大小
   * @return 返回是否传成功
   */
  @Override
  public boolean count(long count) {
    if (!isScheduled) {
      createMonitorTread();
    }
    download += count;
    logger.info("本次下载大小：" + count / 1024 + "KB.");
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 传输结束时，调用end方法.
   */
  @Override
  public void end() {
    logger.info("文件传输结束，以下载：" + download);
    stop();
  }

  /**
   * 创建一个线程每隔一定时间，输出一下上传进度.
   */
  private void createMonitorTread() {
    //检测只用单线程，虽然ScheduledExecutorService很灵活还可以多线程
    executorService = Executors.newSingleThreadScheduledExecutor();
    // scheduleWithFixedDelay()是每次都要把任务执行完成后再延迟固定时间后再执行下一次,1秒钟后开始执行，每2杪钟执行一次
    // this，多态,runnable类
    executorService.scheduleWithFixedDelay(this, 1, 2, TimeUnit.SECONDS);
    isScheduled = true;
  }

  @Override
  public void run() {
    NumberFormat format = NumberFormat.getPercentInstance();
    logger.info("已传输：" + download);
  }


  /**
   * 关闭检测器的线程服务.
   */
  private void stop() {
    boolean isShutdown = executorService.isShutdown();
    if (!isShutdown) {
      executorService.shutdown();
    }
  }
}
