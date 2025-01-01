package com.liushenwuzu.config;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.messages.Item;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传至Minio数据库的工具库.
 */
@Component
public class MinioConfig implements InitializingBean {

  @Value(value = "${minio.bucket}")
  private String bucket;

  @Value(value = "${minio.host}")
  private String host;

  @Value(value = "${minio.url}")
  private String url;

  @Value(value = "${minio.accessKey}")
  private String accessKey;

  @Value(value = "${minio.secretKey}")
  private String secretKey;

  private MinioClient minioClient;

  /**
   * 重载.
   *
   * @throws Exception 数据为空的异常
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.hasText(url, "Minio url 为空");
    Assert.hasText(accessKey, "Minio accessKey为空");
    Assert.hasText(secretKey, "Minio secretKey为空");
    this.minioClient = new MinioClient(this.host, this.accessKey, this.secretKey);
  }


  /**
   * 上传文件，注意的是文件最终在minio上的命名为 用户名+时间+源文件名.
   *
   * @param multipartFile 要上传的文件
   * @param fileName      文件名
   * @return 文件的url
   * @throws Exception error
   */
  public String putObject(MultipartFile multipartFile, String fileName) throws Exception {
    // bucket 不存在，创建
    if (!minioClient.bucketExists(this.bucket)) {
      minioClient.makeBucket(this.bucket);
    }
    try (InputStream inputStream = multipartFile.getInputStream()) {
      // PutObjectOptions，上传配置(文件大小，内存中文件分片大小)
      PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(),
          PutObjectOptions.MIN_MULTIPART_SIZE);
      // 文件的ContentType
      putObjectOptions.setContentType(multipartFile.getContentType());
      minioClient.putObject(this.bucket, fileName, inputStream, putObjectOptions);
      // 返回访问路径
      return this.url + UriUtils.encode(fileName, StandardCharsets.UTF_8);
    }
  }

  /**
   * 删除一个对象 根据文件名删除文件.
   *
   * @param objectName 文件名
   * @return 是否删除成功
   * @throws Exception error
   */
  public boolean removeObject(String objectName) throws Exception {
    String bucketName = this.bucket;
    boolean flag = bucketExists(bucketName);
    if (flag) {
      List<String> objectList = listObjectNames(bucketName);
      for (String s : objectList) {
        if (s.equals(objectName)) {
          minioClient.removeObject(bucketName, objectName);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 其实不需要，随便删除此函数,
   * 因为可以根据url来直接下载源文件.
   *
   * @param fileName 文件名
   * @param response 响应
   */
  public void download(String fileName, HttpServletResponse response) {
    // 从链接中得到文件名
    InputStream inputStream;
    try {
      MinioClient minioClient = new MinioClient(host, accessKey, secretKey);
      ObjectStat stat = minioClient.statObject(bucket, fileName);
      inputStream = minioClient.getObject(bucket, fileName);
      response.setContentType(stat.contentType());
      response.setCharacterEncoding("UTF-8");
      response.setHeader("Content-Disposition", "attachment;filename="
          + URLEncoder.encode(fileName, "UTF-8"));
      IOUtils.copy(inputStream, response.getOutputStream());
      inputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("有异常：" + e);
    }
  }

  /**
   * 检查存储桶是否存在.
   *
   * @param bucketName 桶名
   * @return 是否存在
   * @throws Exception error
   */
  public boolean bucketExists(String bucketName) throws Exception {
    boolean flag = minioClient.bucketExists(bucketName);
    return flag;
  }

  /**
   * 创建存储桶.
   *
   * @param bucketName 桶名
   * @return 是否成功
   * @throws Exception error
   */
  public boolean makeBucket(String bucketName)
      throws Exception {
    boolean flag = bucketExists(bucketName);
    if (!flag) {
      minioClient.makeBucket(bucketName);
      return true;
    } else {
      return false;
    }
  }

  /**
   * 列出存储桶中的所有对象.
   *
   * @param bucketName 存储桶名称
   * @return 一个对象列表
   * @throws Exception error
   */
  public Iterable<Result<Item>> listObjects(String bucketName) throws Exception {
    boolean flag = bucketExists(bucketName);
    if (flag) {
      return minioClient.listObjects(bucketName);
    }
    return null;
  }

  /**
   * 列出存储桶中的所有对象名称.
   *
   * @param bucketName 存储桶名称
   * @return 一个名称列表
   * @throws Exception error
   */
  public List<String> listObjectNames(String bucketName) throws Exception {
    List<String> listObjectNames = new ArrayList<>();
    boolean flag = bucketExists(bucketName);
    if (flag) {
      Iterable<Result<Item>> myObjects = listObjects(bucketName);
      for (Result<Item> result : myObjects) {
        Item item = result.get();
        listObjectNames.add(item.objectName());
      }
    }
    return listObjectNames;
  }


  /**
   * 文件访问路径.
   *
   * @param objectName 存储桶里的对象名称
   * @return 文件url
   * @throws Exception error
   */
  public String getObjectUrl(String objectName) throws Exception {
    String bucketName = this.bucket;
    boolean flag = bucketExists(bucketName);
    String url = "";
    if (flag) {
      url = minioClient.getObjectUrl(bucketName, objectName);
    }
    return url;
  }

}
