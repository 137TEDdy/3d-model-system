package com.liushenwuzu;

import com.liushenwuzu.client.UserClient;

import com.liushenwuzu.pipeline.downloader.Downloader;
import com.liushenwuzu.pipeline.producer.Producer;
import com.liushenwuzu.pipeline.uploader.Uploader;
import com.liushenwuzu.pipeline.utils.ApplicationBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 模型服务启动类
 */
@SpringBootApplication
@EnableSwagger2WebMvc
@EnableFeignClients(clients = UserClient.class)
@Import(ApplicationBeanFactory.class)
public class ModelApplication {

  /**
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(ModelApplication.class, args);
    Uploader uploader = (Uploader) ApplicationBeanFactory.getBean("uploader");
    uploader.start();
    Producer producer = (Producer) ApplicationBeanFactory.getBean("producer");
    producer.start();
    Downloader downloader = (Downloader) ApplicationBeanFactory.getBean("downloader");
    downloader.start();
  }
}
