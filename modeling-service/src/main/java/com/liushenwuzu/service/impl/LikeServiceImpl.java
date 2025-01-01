package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.mapper.LikeMapper;
import com.liushenwuzu.service.LikeService;
import com.liushenwuzu.domain.po.Like;

import org.springframework.stereotype.Service;

/**
 * 喜欢service层
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

}
