package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.service.OnShowService;
import com.liushenwuzu.domain.vo.ViewOnShowVo;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.mapper.ModelMapper;
import com.liushenwuzu.mapper.OnShowMapper;
import com.liushenwuzu.service.IModelService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品接口实现类
 */
@Slf4j
@Service
public class OnShowServiceImpl extends ServiceImpl<OnShowMapper, OnShow> implements OnShowService {

  @Autowired
  OnShowMapper onShowMapper;
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  private IModelService modelService;

  /**
   * 发布商品
   *
   * @param userId  用户id
   * @param modelId 模型id
   * @param price   价格
   * @param note    备注
   * @return 成功/失败
   */
  @Override
  public Result publishOnShow(int userId, int modelId, int price, String note) {
    Model model = modelService.getById(modelId);
    if(model==null){
      return Result.error("模型不存在");
    }
    if(model.getUserId()!=userId){
      return Result.error("这不是你的模型,不能发售");
    }
    if (model.getIsBought() == 1) {
      return Result.error("该模型是购买得来,不能销售");
    }
    List<OnShow>list=this.lambdaQuery().eq(OnShow::getModelId,modelId).eq(OnShow::getFlag,1).list();
    if(!list.isEmpty()){
      return Result.error("此模型已经发售，请勿重复发售");
    }
    OnShow onShow = new OnShow();
    String date = LocalDateTime.now().toString();
    onShow.setNote(note);
    onShow.setPrice(price);
    onShow.setModelId(modelId);
    onShow.setCreatedTime(date);
    onShow.setUserId(userId);
    this.save(onShow);
    return Result.success();
  }

  /**
   * 商户商品列表
   *
   * @param userId 用户id
   * @return 商品列表
   */
  @Override
  public List<ViewOnShowVo> viewOnShow(int userId) {
    List<ViewOnShowVo> viewOnShowVoList = new ArrayList<>();
    QueryWrapper<OnShow> queryWrapper;
    if (userId == 0) {
      queryWrapper = new QueryWrapper<OnShow>().select("*");
      findByUserId(viewOnShowVoList, queryWrapper);
    } else {
      queryWrapper = new QueryWrapper<OnShow>().select("*").eq("user_id", userId);
      findByUserId(viewOnShowVoList, queryWrapper);
    }
    return viewOnShowVoList;
  }

  /**
   * 通过商户id去满足用户
   *
   * @param viewOnShowVoList
   * @param queryWrapper
   */
  private void findByUserId(List<ViewOnShowVo> viewOnShowVoList,
      QueryWrapper<OnShow> queryWrapper) {
    List<OnShow> onShowList = onShowMapper.selectList(queryWrapper);
    for (OnShow onShow : onShowList) {
      if (modelService.getById(onShow.getModelId()).getStatus() == 3) {
        continue;
      }
      if(onShow.getFlag()==0){
        continue;
      }
      ViewOnShowVo viewOnShowVO = new ViewOnShowVo();
      BeanUtils.copyProperties(onShow, viewOnShowVO);
      viewOnShowVO.setOnSaleId(onShow.getOnSaleId());
      findModelInfo(viewOnShowVO);
      viewOnShowVoList.add(viewOnShowVO);
    }
  }

  /**
   * 查找模型信息
   *
   * @param viewOnShowVO 模型表单
   */
  public void findModelInfo(ViewOnShowVo viewOnShowVO) {
    QueryWrapper<Model> queryWrapper = new QueryWrapper<Model>()
        .select("*")
        .eq("model_id", viewOnShowVO.getModelId());
    Model model = modelMapper.selectOne(queryWrapper);
    viewOnShowVO.setCoverUrl(model.getCoverUrl());
    viewOnShowVO.setModelName(model.getName());
    viewOnShowVO.setFilePath(model.getFilePath());
  }

  /**
   * 通过商品id查找model
   *
   * @param onshowId 商品id
   * @return 模型信息
   */
  @Override
  public Model findModel(int onshowId) {
    OnShow onShow = onShowMapper.selectById(onshowId);
    if (onShow == null) {
      return null;
    }
    return modelMapper.selectById(onShow.getModelId());
  }

  /**
   * 修改商品信息
   *
   * @param onSaleId 商品id
   * @param price    价格
   * @param note     备注
   * @return 成功/失败
   */
  @Override
  public boolean modifyOnSale(int onSaleId, int price, String note) {
    OnShow onShow = onShowMapper.selectById(onSaleId);
    if (onShow == null) {
      return false;
    }
    onShow.setNote(note);
    onShow.setPrice(price);
    onShow.setCreatedTime(LocalDateTime.now().toString());
    onShowMapper.updateById(onShow);
    return true;
  }
}
