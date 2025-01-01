package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.Model;

import org.apache.ibatis.annotations.Mapper;

/**
 * 模型映射层.
 */
@Mapper
public interface ModelMapper extends BaseMapper<Model> {
}
