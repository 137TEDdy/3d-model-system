package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liushenwuzu.domain.po.Users;
import com.liushenwuzu.mapper.UsersMapper;
import com.liushenwuzu.service.UsersService;

import org.springframework.stereotype.Service;

/**
 * 服务实现类.
 *
 * @author clk
 * @since 2023-11-14
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
}
