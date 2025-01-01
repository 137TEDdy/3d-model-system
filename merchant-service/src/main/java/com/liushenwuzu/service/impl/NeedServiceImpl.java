package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.liushenwuzu.service.NeedService;
import com.liushenwuzu.domain.po.Need;
import com.liushenwuzu.mapper.NeedMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 需求服务接口实现类
 */
@Service
public class NeedServiceImpl extends ServiceImpl<NeedMapper, Need> implements NeedService {

  @Autowired
  NeedMapper needMapper;

}
