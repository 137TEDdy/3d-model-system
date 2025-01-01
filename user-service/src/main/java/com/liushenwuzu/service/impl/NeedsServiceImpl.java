package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.domain.po.Needs;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.vo.UserQueryNeedVo;
import com.liushenwuzu.mapper.NeedsMapper;
import com.liushenwuzu.mapper.SatisfactionMapper;
import com.liushenwuzu.mapper.UserMapper;
import com.liushenwuzu.service.INeedsService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户发布的3d需求 服务实现类.
 */
@Service
public class NeedsServiceImpl extends ServiceImpl<NeedsMapper, Needs> implements INeedsService {

  @Autowired
  NeedsMapper needsMapper;
  @Autowired
  SatisfactionMapper satisfactionMapper;
  @Autowired
  UserMapper userMapper;

  /**
   * 查询需求
   *
   * @param userId
   * @return {@link List}<{@link UserQueryNeedVo}>
   */
  @Override
  public List<UserQueryNeedVo> queryNeeds(int userId) {
    List<UserQueryNeedVo> userQueryNeedVos = new ArrayList<>();
    List<Needs> needsList;
    if (userId == 0) {
      QueryWrapper<Needs> queryWrapper = new QueryWrapper<Needs>()
          .select("*");
      needsList = needsMapper.selectList(queryWrapper);
      for (Needs needs : needsList) {
        if(needs.getIsSatisfied()==1){
          continue;
        }
        UserQueryNeedVo userQueryNeedVO = new UserQueryNeedVo();
        BeanUtils.copyProperties(needs, userQueryNeedVO);
        User user = userMapper.selectById(needs.getUserId());
        userQueryNeedVO.setNickname(user.getNickname());
        userQueryNeedVO.setAvatarUrl(user.getAvatarUrl());
        userQueryNeedVos.add(userQueryNeedVO);
      }
      return userQueryNeedVos;
    }
    QueryWrapper<Needs> queryWrapper = new QueryWrapper<Needs>()
        .select("*")
        .eq("user_id", userId);
    needsList = needsMapper.selectList(queryWrapper);
    for (Needs needs : needsList) {
      UserQueryNeedVo userQueryNeedVO = new UserQueryNeedVo();
      BeanUtils.copyProperties(needs, userQueryNeedVO);
      User user = userMapper.selectById(needs.getUserId());
      userQueryNeedVO.setNickname(user.getNickname());
      userQueryNeedVO.setAvatarUrl(user.getAvatarUrl());
      userQueryNeedVos.add(userQueryNeedVO);
    }
    return userQueryNeedVos;
  }

  /**
   * 修改需求
   *
   * @param needId
   * @param note
   * @param title
   * @return boolean
   */
  @Override
  public boolean modifyNeeds(int needId, String note, String title) {
    Needs need = needsMapper.selectById(needId);
    need.setNote(note);
    need.setTitle(title);
    needsMapper.updateById(need);
    return true;
  }
}
