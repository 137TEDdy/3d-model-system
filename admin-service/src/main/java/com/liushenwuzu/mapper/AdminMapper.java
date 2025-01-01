package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liushenwuzu.domain.po.Admin;

import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员表 Mapper 接口.
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
