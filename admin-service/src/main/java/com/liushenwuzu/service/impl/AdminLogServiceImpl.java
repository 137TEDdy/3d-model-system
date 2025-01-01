package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liushenwuzu.domain.po.AdminLog;
import com.liushenwuzu.mapper.AdminLogMapper;
import com.liushenwuzu.service.AdminLogService;

import org.springframework.stereotype.Service;

/**
 * 管理员登录日志 服务实现类
 *
 * @author clk
 * @since 2023-11-18
 */
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog>
    implements AdminLogService {
}
