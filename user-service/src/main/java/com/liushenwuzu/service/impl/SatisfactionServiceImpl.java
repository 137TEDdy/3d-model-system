package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.mapper.SatisfactionMapper;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.Satisfaction;
import com.liushenwuzu.domain.vo.QuerySatisVo;
import com.liushenwuzu.service.OnShowService;
import com.liushenwuzu.service.SatisfactionService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户提供满足满足service层
 */
@Service
public class SatisfactionServiceImpl extends
    ServiceImpl<SatisfactionMapper, Satisfaction> implements SatisfactionService {

  @Autowired
  private OnShowService onShowService;

  @Autowired
  private SatisfactionMapper satisfactionMapper;

  /**
   * 查询满足
   *
   * @param needId
   * @return {@link List}<{@link QuerySatisVo}>
   */
  @Override
  public List<QuerySatisVo> querySatis(int needId) {
    List<QuerySatisVo> querySatisVoList = new ArrayList<>();
    QueryWrapper<Satisfaction> queryWrapper = new QueryWrapper<Satisfaction>()
        .select("*")
        .eq("need_id", needId);
    List<Satisfaction> satisfactions = satisfactionMapper.selectList(queryWrapper);
    for (Satisfaction satisfaction : satisfactions) {
      QuerySatisVo querySatisVO = new QuerySatisVo();
      BeanUtils.copyProperties(satisfaction, querySatisVO);
      OnShow onshow = onShowService.getById(satisfaction.getOnSaleId());
      if(onshow.getFlag()==0){
        continue;
      }
      Model model = onShowService.findModel(satisfaction.getOnSaleId());
      if (model == null || model.getStatus() == 3) {
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

  /**
   * @param satisfactionId
   * @param choice         0:未审核 1：用户选择 2：用户拒绝
   * @return
   */
  @Override
  public Integer selectSatis(int userId, int satisfactionId, int choice) {
    Satisfaction satisfaction = satisfactionMapper.selectById(satisfactionId);
    if(satisfaction==null){
      return -1;
    }
    if (satisfaction.getIsChosen() != 0) {
      return 0;
    }
    if(choice==1){
      satisfaction.setIsChosen(choice);
    } else if (choice==2) {
      satisfaction.setIsChosen(choice);
    }
    satisfactionMapper.updateById(satisfaction);
    return satisfaction.getOnSaleId();
  }
}
