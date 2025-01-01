package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.liushenwuzu.domain.po.Image;

import org.apache.ibatis.annotations.Mapper;

/**
 * 图片mapper层
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {

}
