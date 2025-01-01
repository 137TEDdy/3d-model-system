package com.liushenwuzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liushenwuzu.domain.po.Users;

import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口.
 *
 * @author clk
 * @since 2023-11-14
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
}
