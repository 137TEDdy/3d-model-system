package com.liushenwuzu.pipeline.downloader;

import com.aspose.threed.FileFormat;
import com.aspose.threed.Scene;
import com.jcraft.jsch.JSchException;

import com.liushenwuzu.config.MinioConfig;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.pipeline.TaskQueue;
import com.liushenwuzu.pipeline.utils.Compressor;
import com.liushenwuzu.pipeline.utils.JschUtil;
import com.liushenwuzu.pipeline.utils.RandomString;
import com.liushenwuzu.service.ModelService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载转换线程.
 */
@Component
public class Downloader extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(Downloader.class);
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
  private String identification;
  private String localDirectory;
  private Integer modelId;

  private JschUtil plyServer; //连接器

  @Autowired
  private ModelService modelService;
  @Autowired
  private MinioConfig minioConfig;


  public Downloader(DownloadTaskQueueImpl queue) {
    this.taskQueue = queue;
  }

  @Override
  public void run() {
    DownloadTask downloadTask;
    while (true) {
      downloadTask = (DownloadTask) taskQueue.pop();
      if (downloadTask != null) {
        logger.info("执行Download任务：" + downloadTask.getIdentification());
        identification = downloadTask.getIdentification();
        localDirectory = downloadTask.getLocalDirectory();
        modelId = downloadTask.getModelId();
        handTask();
      } else {
        mySleep();
      }
    }
  }

  /**
   * 包装方法下载目标文件.
   *
   * @throws Exception IO异常
   */
  private void remoteDownload() throws Exception {
    String fileName = "test_texture_" + identification;
    plyServer.download("/home/zhujiada/mvgImages/output/files-" + identification
            + "/" + fileName + ".ply",
        new File(localDirectory, fileName + ".ply").getAbsolutePath());
    plyServer.download("/home/zhujiada/mvgImages/output/files-" + identification
            + "/" + fileName + ".png",
        new File(localDirectory, fileName + ".png").getAbsolutePath());
  }

  /**
   * 文件类型转换，并上传至MINIO数据库.
   *
   * @return MINIO路径
   * @throws Exception error
   */
  private String transfer() throws Exception {
    String outputUrl;
    //转换文件格式为glb，并上传至minio
    try {
      String fileName = "test_texture_" + identification;
      String plyName = new File(localDirectory, fileName + ".ply").getAbsolutePath();
      String pngName = new File(localDirectory, fileName + ".png").getAbsolutePath();

      // 加载源 PLY 文件，并转化为glb
      Scene scene = new Scene(plyName);
      String glbName = new File(localDirectory, fileName + ".glb").getAbsolutePath();
      // 将 3D 场景转换为二进制 GLTF 格式的文件
      scene.save(glbName, FileFormat.GLTF2__BINARY);

      //压缩三个文件
      List<String> names = new ArrayList<>();
      names.add(plyName);
      names.add(pngName);
      names.add(glbName);
      String target = Compressor.compressFiles(names);

      //存minio
      File file = new File(target);
      MultipartFile multipartFile = file2MultipartFile(file);
      //随机化名字防止被破解
      String randName = RandomString.getRandomString(32);
      outputUrl = minioConfig.putObject(multipartFile, randName + ".rar");
    } catch (IOException e) {
      logger.info("succeed to product, but fail to transfer because of" + e.getMessage());
      throw new Exception("fail to transfer");
    }
    return outputUrl;
  }

  private void handTask() {
    try {
      //连接
      if (plyServer == null) {
        plyServer = new JschUtil(address, port, username, password);
      }
      boolean connect = plyServer.connect();
      if (!connect) {
        throw new JSchException("模型服务器连接失败");
      }

      //下载指定模型文件
      remoteDownload();
      plyServer.close();
      String outputUrl = transfer();
      logger.info("资源路径："+outputUrl);
      //生成成功,改status0
      Model model = new Model();
      model.setStatus(0);
      model.setFilePath(outputUrl);
      model.setModelId(modelId);
      modelService.updateById(model);
      logger.info("succeed to product" + outputUrl);
    } catch (Exception e) {
      logger.info(e.getMessage());
      //生成失败,改status1
      Model model = new Model();
      model.setStatus(1);
      model.setModelId(modelId);
      modelService.updateById(model);
      logger.warn("fail to product");
    }
    //删本地文件夹
    File file = new File(localDirectory);
    if (file.exists()) {
      deleteFolder(file);
    }
  }

  /**
   * 递归删文件夹.
   *
   * @param folder 文件夹
   */
  private void deleteFolder(File folder) {
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          deleteFolder(file);
        } else {
          file.delete();
        }
      }
    }
    folder.delete();
  }

  /**
   * file转MultipartFile类.
   *
   * @param file file对象
   * @return 转换对象
   */
  private MultipartFile file2MultipartFile(File file) {
    FileItem fileItem = creatFileItem(file);
    MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
    return multipartFile;
  }

  /**
   * file2MultipartFile的支持函数，转FileItem类.
   *
   * @param file file对象
   * @return 转换对象
   */
  private FileItem creatFileItem(File file) {

    DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(16, null);
    FileItem fileItem = diskFileItemFactory.createItem("textField",
        "application/zip", true, file.getName());

    int bytesRead = 0;
    byte[] buffer = new byte[8192];

    try {
      FileInputStream fileInputStream = new FileInputStream(file);
      OutputStream outputStream = fileItem.getOutputStream();
      while ((bytesRead = fileInputStream.read(buffer, 0, 8192)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
      Files.copy(file.toPath(), outputStream);
      outputStream.close();
      fileInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileItem;
  }

  private final Object waitObj = new Object();

  /**
   * 关闭连接资源，使线程进入WAITING状态.
   */
  private void mySleep() {
    //关ply连接，归还资源
    if (plyServer != null) {
      plyServer.close();
      plyServer = null;
    }
    synchronized (waitObj) { //使用wait必须的同步
      try {
        logger.warn("Download队列中无任务，Downloader进入WAITING状态");
        waitObj.wait();
        logger.info("Downloader已被重新唤醒");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 唤醒线程.
   */
  public void wakeUp() {
    synchronized (waitObj) {
      waitObj.notify();
    }
  }
}
