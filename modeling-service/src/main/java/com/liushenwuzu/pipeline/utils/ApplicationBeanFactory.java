package com.liushenwuzu.pipeline.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 产生bean工厂.
 */
public class ApplicationBeanFactory implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;

  /**
   * 设置应用内容.
   *
   * @param applicationContext springboot的bean的上下文
   * @throws BeansException error
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (ApplicationBeanFactory.applicationContext == null) {
      ApplicationBeanFactory.applicationContext = applicationContext;
    }
  }

  /**
   * 得到应用.
   *
   * @return {@link ApplicationContext}
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 得到bean.
   *
   * @param name bean名称
   * @return {@link Object}
   */
  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

}
