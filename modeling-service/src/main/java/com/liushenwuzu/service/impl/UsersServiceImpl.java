package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Like;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.Share;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.vo.ModelVo;
import com.liushenwuzu.mapper.UsersMapper;
import com.liushenwuzu.service.IUsersService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author clk
 * @since 2023 -12-15
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, User> implements IUsersService {

  /**
   * 查询某用户所有分享过的模型
   *
   * @param userId
   * @return {@link Result}
   */
  @Override
  public Result ownPublished(Integer userId) {
    //查询某用户所有发布的模型，得到模型ID列表
    List<Share> shares = Db.lambdaQuery(Share.class)
        .eq(Share::getUserId, userId).list();
    List<Integer> ids = shares.stream().map(Share::getModelId).collect(Collectors.toList());
    List<ModelVo> modelVos = new ArrayList<>(ids.size());
    //将model值赋给modelVO，并设置nickname
    for (Integer id : ids) {
      Model model = Db.getById(id, Model.class);
      if (model == null) {
        continue;
      }
      ModelVo modelVO = new ModelVo();
      BeanUtils.copyProperties(model, modelVO);
      modelVO.setIsLiked(0);
      modelVO.setNickname(this.getById(model.getUserId()).getNickname());
      modelVos.add(modelVO);
    }
    //得到收藏列表
    List<Like> likes = Db.lambdaQuery(Like.class)
        .eq(Like::getUserId, userId)
        .list();
    List<Integer> likedModelIds = likes.stream().map(Like::getModelId).collect(Collectors.toList());
    //设置用户是否收藏
    for (ModelVo modelVO : modelVos) {
      if (likedModelIds.contains(modelVO.getModelId())) {
        modelVO.setIsLiked(1);
      }
    }
    return Result.success(modelVos);
  }
}
