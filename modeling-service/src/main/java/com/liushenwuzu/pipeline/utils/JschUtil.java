package com.liushenwuzu.pipeline.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * JSch连接工具，通过ssh协议连接控制其他服务器.
 */
public class JschUtil {

  private static final Logger logger = LoggerFactory.getLogger(JschUtil.class);
  private JSch jsch;
  private Session session;
  private Channel channel;
  private ChannelSftp chSftp;
  private String host;
  private int port;
  private String username;
  private String password;
  private boolean busy;

  public void setBusy() {
    busy = true;
  }

  public void setSpare() {
    busy = false;
  }

  /**
   * 传入连接服务器的信息，创建连接工具.
   *
   * @param host     IP
   * @param port     端口
   * @param username 用户名
   * @param password 密码
   */
  public JschUtil(String host, int port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    setSpare();
  }

  /**
   * 连接方法，建立JSch连接对象.
   *
   * @return 返回是否连接成功
   */
  public boolean connect() {
    jsch = new JSch();
    boolean result = false;
    try {
      session = jsch.getSession(username, host, port); // 根据主机账号、ip、端口获取一个Session对象
      session.setPassword(password); // 存放主机密码

      Properties config = new Properties(); // 首次连接，去掉公钥确认
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);

      session.setTimeout(3000); // 超时连接时间为3秒
      session.connect(); // 进行连接
      result = session.isConnected(); // 获取连接结果
      if (!result) {
        System.err.println("【连接】获取连接失败");
      }
    } catch (JSchException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 关闭连接资源.
   */
  public void close() {
    if (channel != null && channel.isConnected()) { //执行命令的通道关掉
      channel.disconnect();
    }

    if (session != null && session.isConnected()) { //会话通道关掉
      session.disconnect();
    }

    if (chSftp != null && chSftp.isConnected()) { //文件通道关掉
      chSftp.quit();
    }
  }

  /**
   * 传回Sudo命令执行信息.
   *
   * @param in  输入流
   * @param out 向服务器的输出流
   * @throws Exception IOException
   */
  private void printProcedureSudoMessage(InputStream in, OutputStream out) throws Exception {
    int exitStatus = 0;
    //接收返回，阻塞主要在这里，要等待命令接收回
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    while (true) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException excepted) {
      }
      while (true) {
        String msg = reader.readLine();
        if (msg == null) break;
        logger.info(msg);
      }
      if (channel.isClosed()) { // 从channel获取全部信息之后，channel会自动关闭
        if (in.available() > 0) {
          continue;
        }
        exitStatus = channel.getExitStatus();
        break;
      }
    }
    logger.info("------------------------------------------------");
    logger.info("命令执行退出码为：" + exitStatus);
    in.close(); //关闭流
    out.close();
  }

  /**
   * 通过ssh连接执行命令行sudo命令.
   *
   * @param command 需要执行的命令
   * @throws Exception 连接异常
   */
  public void execSudoCommand(String command) throws JSchException, IOException {
    if (!session.isConnected()) {
      throw new JSchException("执行脚本：尚未连接到服务器");
    }

    channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand("sudo -S -p '' " + command); // sudo权限
    OutputStream out = channel.getOutputStream();

    channel.connect(); // 执行命令，等待执行结果
    out.write((password + "\n").getBytes()); // sudo需要向其输入密码
    out.flush();

    InputStream in = channel.getInputStream(); // 获取命令执行结果
    //将错误流重定向到日志文件
    PrintStream errLog = new PrintStream("/root/mylog/openMVG.log");
    errLog.println(LocalDateTime.now());
    ((ChannelExec) channel).setErrStream(errLog);
    try {
      printProcedureSudoMessage(in, out);
    } catch (Exception expected) {
    }
    errLog.close();
  }

  /**
   * 传回普通命令执行信息.
   *
   * @param in 输入流
   * @throws Exception IOException
   */
  private void printProcedureMessage(InputStream in) throws IOException {
    int exitStatus = 0;
    //接收返回，阻塞主要在这里，要等待命令接收回
    byte[] tmp = new byte[1024];
    while (true) {
      while (in.available() > 0) {
        int i = in.read(tmp, 0, 1024);
        if (i < 0) {
          break;
        }
        System.out.print(new String(tmp, 0, i)); // 输出命令执行过程
        // TODO 或许需要执行监测
      }
      if (channel.isClosed()) { // 从channel获取全部信息之后，channel会自动关闭
        if (in.available() > 0) {
          continue;
        }
        exitStatus = channel.getExitStatus();
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (Exception excepted) {
      }
    }
    logger.info("------------------------------------------------");
    logger.info("命令执行退出码为：" + exitStatus);

    in.close(); //关闭流
  }

  /**
   * 通过ssh连接执行命令行普通命令.
   *
   * @param command 需要执行的命令
   * @throws Exception 连接异常
   */
  public void execCommand(String command) throws JSchException, IOException {
    if (!session.isConnected()) {
      throw new JSchException("执行脚本：尚未连接到服务器");
    }

    channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand(command);
    ((ChannelExec) channel).setErrStream(System.err); // 错误信息输出流，用于输出错误的信息，当exitStatus<0的时候
    channel.connect(); // 执行命令，等待执行结果
    InputStream in = channel.getInputStream(); // 获取命令执行结果

    printProcedureMessage(in);
  }

  /**
   * ssh上传文件.
   *
   * @param localFile  本地的文件路径
   * @param uploadPath 远程的目的路径地址
   * @throws Exception error
   */
  public void upload(String localFile, String uploadPath) throws JSchException, IOException {
    if (!session.isConnected()) {
      throw new JSchException("上传文件：尚未连接到服务器");
    }
    if (chSftp == null) {
      chSftp = (ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道
      chSftp.connect(); // 建立STFP连接
      try {
        chSftp.setFilenameEncoding("UTF-8"); // 设置文件名的编码格式
      } catch (SftpException expected) {
      }
    }

    // 查看文件上传进度，另开一个线程
    UploadMonitor monitor = new UploadMonitor(new File(localFile).length());
    try {
      if (this.busy) { //模型服务器繁忙，额外检测进度
        OutputStream out = chSftp.put(uploadPath, monitor, ChannelSftp.OVERWRITE);
        byte[] buff = new byte[1024 * 10]; // 设定每次传输的数据块大小为10KB
        int read;
        // 手动一个缓存块一个缓存块的发
        if (out != null) {
          FileInputStream fis = new FileInputStream(localFile);
          do {
            //读一块发一块
            read = fis.read(buff, 0, buff.length);
            if (read > 0) {
              out.write(buff, 0, read);
            }
            out.flush();
          } while (read >= 0);
        }
      } else {
        chSftp.put(localFile, uploadPath);
      }
    } catch (SftpException expected) {
    }
    logger.info("文件上传成功");
  }

  /**
   * ssh下载文件.
   *
   * @param directory    本地的目录路径
   * @param downloadFile 远程的文件地址
   * @throws Exception error
   */
  public void download(String directory, String downloadFile) throws Exception {
    if (!session.isConnected()) {
      throw new JSchException("下载文件：尚未连接到服务器");
    }

    if (chSftp == null) {
      chSftp = (ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道
      chSftp.connect(); // 建立STFP连接
      chSftp.setFilenameEncoding("UTF-8"); // 设置编码格式
    }

    if (this.busy) { //繁忙检测
      chSftp.get(directory, downloadFile, new DownloadMonitor(), ChannelSftp.RESUME);
    } else {
      chSftp.get(directory, downloadFile);
    }
    logger.info("文件下载成功");
  }
}

