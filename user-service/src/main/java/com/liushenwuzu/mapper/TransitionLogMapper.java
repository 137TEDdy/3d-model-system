package com.liushenwuzu.mapper;

import com.liushenwuzu.domain.po.TransitionLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 个人交易日志 Mapper 接口
 *
 * @author clk
 * @since 2023-12-20
 */
@Mapper
public interface TransitionLogMapper extends BaseMapper<TransitionLog> {

}
