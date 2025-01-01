package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.common.utils.JwtUtils;
import com.liushenwuzu.domain.dto.AdminLoginFormDto;
import com.liushenwuzu.domain.po.Admin;
import com.liushenwuzu.domain.po.AdminLog;
import com.liushenwuzu.domain.vo.AdminVo;
import com.liushenwuzu.mapper.AdminMapper;
import com.liushenwuzu.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员表 服务实现类.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

  /**
   * 管理员登录.
   */
  public Result login(AdminLoginFormDto loginFormDTO) {
    //如果电话号码或者密码为空
    if (loginFormDTO.getPhone() == null || loginFormDTO.getPassword() == null) {
      return Result.error("电话号码或密码不能为空");
    }
    //根据电话和密码查找管理员
    Admin admin = this.lambdaQuery()
        .eq(Admin::getPhone, loginFormDTO.getPhone()).one();
    if (admin == null) {
      return Result.error("手机号错误");
    }
    //设置管理员登录日志
    AdminLog adminLog = new AdminLog();
    adminLog.setAdminId(admin.getAdminId());
    adminLog.setLogTime(LocalDateTime.now().toString());
    adminLog.setIp(loginFormDTO.getIp());
    //如果登录失败
    if (!loginFormDTO.getPassword().equals(admin.getPassword())) {
      adminLog.setStatus(0);
      Db.save(adminLog);
      return Result.error("密码错误");
    }
    //生成TOKEN
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", admin.getAdminId());
    claims.put("username", admin.getNickname());
    String token = JwtUtils.generateJwt(claims);
    AdminVo adminVo = new AdminVo();
    BeanUtils.copyProperties(admin, adminVo);
    adminVo.setToken(token);
    //登录成功
    adminLog.setStatus(1);
    Db.save(adminLog);
    return Result.success(adminVo);
  }
}
