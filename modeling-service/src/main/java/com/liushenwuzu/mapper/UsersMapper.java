package com.liushenwuzu.mapper;

import com.liushenwuzu.domain.po.User;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 */
@Mapper
public interface UsersMapper extends BaseMapper<User> {

}
