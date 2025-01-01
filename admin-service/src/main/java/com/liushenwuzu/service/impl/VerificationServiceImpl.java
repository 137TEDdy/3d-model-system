package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liushenwuzu.domain.po.Verification;
import com.liushenwuzu.mapper.VerificationMapper;
import com.liushenwuzu.service.VerificationService;

import org.springframework.stereotype.Service;

/**
 * 认证审核 服务实现类.
 */
@Service
public class VerificationServiceImpl extends ServiceImpl<VerificationMapper, Verification>
    implements VerificationService {
}
