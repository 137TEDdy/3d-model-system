package com.liushenwuzu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 商户微服务启动类
 */
@SpringBootApplication
@EnableSwagger2WebMvc
public class MerchantApplication {

  /**
   * @param args
   */
  public static void main(String[] args) {

    SpringApplication.run(MerchantApplication.class, args);
  }

}

