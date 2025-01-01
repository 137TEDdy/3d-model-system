package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.service.MerchantService;
import com.liushenwuzu.domain.po.Merchant;
import com.liushenwuzu.domain.vo.MerchantVo;
import com.liushenwuzu.mapper.MerchantMapper;
import com.liushenwuzu.common.utils.JwtUtils;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商户服务接口实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class
MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

  @Autowired
  private MerchantMapper merchantMapper;

  /**
   * 商户登录
   *
   * @param phone    电话号码
   * @param password 密码
   * @return 商户登录表单
   */
  @Override
  public Result login(String phone, String password) {
    QueryWrapper<Merchant> queryWrapper = new QueryWrapper<Merchant>()
        .select("*")
        .eq("phone", phone);
    Merchant merchant = merchantMapper.selectOne(queryWrapper);
    if(merchant==null){
      return Result.error("该手机号码未注册");
    }
    if (!Objects.equals(merchant.getPassword(), password)){
      return Result.error("密码错误");
    }
    if(merchant.getIsMerchant()==0){
      return Result.error("您还不是商户");
    }
    MerchantVo merchantVO = new MerchantVo();
    merchantVO.setNickname(merchant.getNickname());
    BeanUtils.copyProperties(merchant, merchantVO);
    // 5.生成TOKEN
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", merchantVO.getUserId());
    claims.put("username", merchantVO.getNickname());
    String token = JwtUtils.generateJwt(claims);
    merchantVO.setToken(token);
    return Result.success(merchantVO);
  }
}
