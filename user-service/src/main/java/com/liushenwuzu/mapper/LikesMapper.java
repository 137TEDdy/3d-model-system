package com.liushenwuzu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.Likes;

import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏列表映射层.
 */
@Mapper
public interface LikesMapper extends BaseMapper<Likes> {

}
