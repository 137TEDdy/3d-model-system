package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.domain.po.Model;

/**
 * 模型service层接口
 */
public interface ModelService extends IService<Model> {

  /**
   * 保存模型
   *
   * @param model
   */
  void saveModel(Model model);
}
