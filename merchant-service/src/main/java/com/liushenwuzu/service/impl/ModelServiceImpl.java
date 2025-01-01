package com.liushenwuzu.service.impl;

import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.mapper.ModelMapper;
import com.liushenwuzu.service.IModelService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * 模型表 服务实现类
 *
 * @author clk
 * @since 2023-12-18
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService {

}
