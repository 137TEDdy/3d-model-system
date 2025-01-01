package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.mapper.ImageMapper;
import com.liushenwuzu.service.ImageService;
import com.liushenwuzu.domain.po.Image;

import org.springframework.stereotype.Service;

/**
 * 图片service层实现
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

}