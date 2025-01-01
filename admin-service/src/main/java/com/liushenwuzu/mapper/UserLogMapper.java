package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liushenwuzu.domain.po.UserLog;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户登录日志 Mapper 接口.
 */
@Mapper
public interface UserLogMapper extends BaseMapper<UserLog> {
}
