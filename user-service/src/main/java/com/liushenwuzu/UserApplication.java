package com.liushenwuzu;

import com.liushenwuzu.client.ModelClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


/**
 * @author 陈立坤
 * @date 2023/12/20
 */
@SpringBootApplication
@EnableSwagger2WebMvc
@EnableFeignClients(clients = ModelClient.class)
public class UserApplication {

  /**
   * @param args
   */
  public static void main(String[] args) {

    SpringApplication.run(UserApplication.class, args);
  }
}
