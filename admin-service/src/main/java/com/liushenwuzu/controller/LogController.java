package com.liushenwuzu.controller;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.AdminLog;
import com.liushenwuzu.domain.po.TransitionLog;
import com.liushenwuzu.domain.po.UserLog;
import com.liushenwuzu.domain.vo.TransitionLogVo;
import com.liushenwuzu.service.ITransitionLogService;
import com.liushenwuzu.service.impl.AdminLogServiceImpl;
import com.liushenwuzu.service.impl.UserLogServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 访问日志的controller.
 */
@RestController
@Api(tags = "日志相关接口")
@RequestMapping("/api/admin/log")
@Slf4j
public class LogController {
  @Autowired
  private UserLogServiceImpl userLogService;
  @Autowired
  private AdminLogServiceImpl adminLogService;
  @Autowired
  private ITransitionLogService transitionLogService;

  /**
   * 管理员产看用户登录日志.
   *
   * @param userId 用户id
   * @return 执行信息
   */
  @ApiOperation("产看用户日志")
  @GetMapping("/users")
  public Result checkUserLog(@RequestParam Integer userId) {
    List<UserLog> userLogs;

    if (userId == 0) { // 查看所有用户登录日志
      userLogs = userLogService.list();
    } else { // 查看特定用户登录日志
      userLogs = userLogService.lambdaQuery()
          .eq(UserLog::getUserId, userId).list();
    }
    return Result.success(userLogs);
  }

  /**
   * 查看管理员登录日志.
   *
   * @param adminId 管理员Id
   * @return 执行信息
   */
  @ApiOperation("查看管理员登录日志")
  @GetMapping("/admin")
  public Result checkAdminLog(@RequestParam Integer adminId) {
    List<AdminLog> adminLogs;
    if (adminId == 0) { //产看所有管理员登录日志
      adminLogs = adminLogService.lambdaQuery().list();
    } else { //产看特定管理员登录日志
      adminLogs = adminLogService.lambdaQuery()
          .eq(AdminLog::getAdminId, adminId).list();
    }
    return Result.success(adminLogs);
  }
  @ApiOperation("查看用户交易日志")
  @GetMapping("/vip")
  public Result transitionLog(@RequestParam Integer userId){
    if(userId!=0) {
      List<TransitionLog> list = transitionLogService.lambdaQuery()
          .eq(TransitionLog::getUserId, userId).list();
      return Result.success(list);
    }
    List<TransitionLog> list = transitionLogService.lambdaQuery().list();
    return Result.success(list);
  }
}
