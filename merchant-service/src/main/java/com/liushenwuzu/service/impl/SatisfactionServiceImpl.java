package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Need;
import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.domain.po.SalesLog;
import com.liushenwuzu.service.OnShowService;
import com.liushenwuzu.service.SalesLogService;
import com.liushenwuzu.service.SatisfactionService;
import com.liushenwuzu.domain.po.Satisfaction;
import com.liushenwuzu.domain.vo.QuerySatisVo;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.mapper.NeedMapper;
import com.liushenwuzu.mapper.SatisfactionMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 满足接口实现类
 */
@Service
public class SatisfactionServiceImpl extends
    ServiceImpl<SatisfactionMapper, Satisfaction> implements SatisfactionService {

  @Autowired
  OnShowService onShowService;

  @Autowired
  SatisfactionMapper satisfactionMapper;

  @Autowired
  NeedMapper needMapper;
  @Autowired
  private ModelServiceImpl modelService;
  @Autowired
  private SalesLogService salesLogService;

  /**
   * 满足需求
   *
   * @param userId   商户id
   * @param needId   需求id
   * @param onshowId 商品id
   * @return 成功/失败
   */
  @Override
  public Result addSatis(int userId, int needId, int onshowId) {
    OnShow onShow = onShowService.getById(onshowId);
    Satisfaction satisfaction = new Satisfaction();
    satisfaction.setNeedId(needId);
    satisfaction.setUserId(userId);
    if (onShow == null || onShow.getFlag() == 0) {
      return Result.error("该商品并未上架");
    }
    Need need = Db.getById(needId, Need.class);
    if (need.getUserId() == userId) {
      return Result.error("这是自己发布的需求,不能提供推荐商品");
    }
    if (need.getIsSatisfied() == 1) {
      return Result.error("此需求已经得到满足,不能再推荐商品");
    }
    Integer modelId = modelService.getById(onShow.getModelId()).getModelId();
    List<SalesLog> salesLogs = salesLogService.lambdaQuery()
        .eq(SalesLog::getModelId, modelId).eq(SalesLog::getTargetUserId, need.getUserId()).list();
    if (!salesLogs.isEmpty()) {
      return Result.error("该用户已经购买过该商品了，不能再推荐商品");
    }
    List<Satisfaction> list = this.lambdaQuery().eq(Satisfaction::getNeedId, needId)
        .eq(Satisfaction::getUserId, userId).eq(Satisfaction::getIsChosen, 0).list();
    if (!list.isEmpty()) {
      return Result.error("您已经提供推荐商品，正在等待回复,请勿重复提供");
    }
    Model model = modelService.getById(onShow.getModelId());
    if (model.getUserId() != userId) {
      return Result.error("这不是你的商品，不能推荐商品");
    }
    String modelPath = model.getFilePath();
    satisfaction.setModelUrl(modelPath);
    satisfaction.setCreatedTime(LocalDateTime.now().toString());
    satisfaction.setPrice(onShow.getPrice());
    satisfaction.setIsChosen(0);
    satisfaction.setOnSaleId(onshowId);
    Db.save(satisfaction);
    return Result.success(satisfaction);
  }

  /**
   * 查看商户满足的需求
   *
   * @param userId 商户id
   * @return 满足列表
   */
  @Override
  public List<QuerySatisVo> querySatis(int userId) {
    List<QuerySatisVo> querySatisVoList = new ArrayList<>();
    QueryWrapper<Satisfaction> queryWrapper = new QueryWrapper<Satisfaction>()
        .select("*")
        .eq("user_id", userId);
    List<Satisfaction> satisfactions = satisfactionMapper.selectList(queryWrapper);
    for (Satisfaction satisfaction : satisfactions) {
      QuerySatisVo querySatisVO = new QuerySatisVo();
      BeanUtils.copyProperties(satisfaction, querySatisVO);
      Model model = modelService.getById(
          onShowService.getById(satisfaction.getOnSaleId()).getModelId());
      if (model == null) {
        continue;
      }
      querySatisVO.setCoverUrl(model.getCoverUrl());
      querySatisVO.setModelId(model.getModelId());
      querySatisVO.setModelName(model.getName());
      querySatisVO.setPrice(satisfaction.getPrice());
      querySatisVoList.add(querySatisVO);
    }
    return querySatisVoList;
  }
}
