package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.Merchant;

import org.apache.ibatis.annotations.Mapper;

/**
 * 商户表mapper层
 */
@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {

}
