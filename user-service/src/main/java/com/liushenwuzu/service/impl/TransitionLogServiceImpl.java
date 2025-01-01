package com.liushenwuzu.service.impl;

import com.liushenwuzu.domain.po.TransitionLog;
import com.liushenwuzu.mapper.TransitionLogMapper;
import com.liushenwuzu.service.ITransitionLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 个人交易日志 服务实现类
 */
@Service
public class TransitionLogServiceImpl extends
    ServiceImpl<TransitionLogMapper, TransitionLog> implements ITransitionLogService {

}
