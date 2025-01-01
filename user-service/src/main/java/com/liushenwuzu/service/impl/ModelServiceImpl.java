package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.mapper.ModelMapper;
import com.liushenwuzu.service.ModelService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * @author totoro
 * @date 2023/12/19
 */
@Slf4j
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
  @Override
  public void saveModel(Model model) {
    save(model);
  }
}
