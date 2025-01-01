package com.liushenwuzu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


/**
 * @author 陈立坤
 * @date 2023/12/20
 */
@SpringBootApplication
@EnableSwagger2WebMvc
public class AdminApplication {

  /**
   * @param args
   */
  public static void main(String[] args) {

    SpringApplication.run(AdminApplication.class, args);
  }
}
