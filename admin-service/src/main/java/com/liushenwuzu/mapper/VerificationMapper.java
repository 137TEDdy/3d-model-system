package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liushenwuzu.domain.po.Verification;

import org.apache.ibatis.annotations.Mapper;

/**
 * 认证审核 Mapper 接口.
 *
 * @author clk
 * @since 2023-11-14
 */
@Mapper
public interface VerificationMapper extends BaseMapper<Verification> {
}
