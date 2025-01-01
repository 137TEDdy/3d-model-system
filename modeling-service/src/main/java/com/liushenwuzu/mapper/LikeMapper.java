package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.Like;

import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏mapper层
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {

}
