package com.liushenwuzu.service.impl;

import com.liushenwuzu.domain.po.UserLog;
import com.liushenwuzu.mapper.UserLogMapper;
import com.liushenwuzu.service.IUserLogService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * 用户登录日志 服务实现类
 *
 * @author clk
 * @since 2023-11-12
 */
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements
    IUserLogService {

}
