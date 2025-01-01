package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.domain.po.Likes;
import com.liushenwuzu.mapper.LikesMapper;
import com.liushenwuzu.service.ILikesService;

import org.springframework.stereotype.Service;

/**
 * 收藏列表 服务实现类.
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements ILikesService {

}
