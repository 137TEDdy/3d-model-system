package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.User;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户映射层.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
