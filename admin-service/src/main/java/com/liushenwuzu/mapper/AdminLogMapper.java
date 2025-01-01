package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liushenwuzu.domain.po.AdminLog;

import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员登录日志 Mapper 接口.
 *
 * @author clk
 * @since 2023-11-18
 */
@Mapper
public interface AdminLogMapper extends BaseMapper<AdminLog> {
}