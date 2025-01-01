package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.mapper.OnShowMapper;
import com.liushenwuzu.service.ModelService;
import com.liushenwuzu.service.OnShowService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 在售模型service层
 */
@Slf4j
@Service
public class OnShowServiceImpl extends ServiceImpl<OnShowMapper, OnShow> implements OnShowService {

  @Autowired
  private OnShowMapper onShowMapper;
  @Autowired
  private ModelService modelService;


  /**
   * 查找模型
   *
   * @param onshowId
   * @return {@link Model}
   */
  @Override
  public Model findModel(int onshowId) {
    OnShow onShow = onShowMapper.selectById(onshowId);
    if (onShow == null) {
      return null;
    }
    Model model = modelService.getById(onShow.getModelId());
    return model;
  }

}
