package com.liushenwuzu.controller;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.dto.ChangePasswordFormDto;
import com.liushenwuzu.domain.dto.AdminLoginFormDto;
import com.liushenwuzu.domain.po.Admin;
import com.liushenwuzu.domain.po.Users;
import com.liushenwuzu.domain.po.Verification;
import com.liushenwuzu.service.impl.AdminServiceImpl;
import com.liushenwuzu.service.impl.UsersServiceImpl;
import com.liushenwuzu.service.impl.VerificationServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理员的controller，各种管理员的业务.
 */
@Api(tags = "管理员相关接口")
@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
  @Autowired
  private AdminServiceImpl adminService;
  @Autowired
  private UsersServiceImpl usersService;
  @Autowired
  private VerificationServiceImpl verificationService;

  /**
   * 管理员根据旧密码和电话修改密码.
   *
   * @param changePasswordFormDTO 接口表单
   * @return 返回信息
   */
  @ApiOperation("修改密码相关接口")
  @PostMapping("/changePassword")
  public Result changePassword(@RequestBody ChangePasswordFormDto changePasswordFormDTO) {
    //根据电话和密码查找管理员
    Admin admin = adminService.lambdaQuery()
        .eq(Admin::getPhone, changePasswordFormDTO.getPhone())
        .eq(Admin::getPassword, changePasswordFormDTO.getOldPassword()).one();
    //如果管理员密码错误
    if (admin == null) {
      return Result.error("旧密码错误");
    }
    //旧密码正确但是旧密码和新密码一样
    if (changePasswordFormDTO.getNewPassword().equals(changePasswordFormDTO.getOldPassword())) {
      return Result.error("新旧密码不能相同");
    }
    //更新管理员密码为新密码
    admin.setPassword(changePasswordFormDTO.getNewPassword());
    //修改数据库
    adminService.updateById(admin);
    return Result.success();
  }

  /**
   * 管理员根据电话和密码登录.
   *
   * @param adminLoginFormDTO 接口表单
   * @return 返回信息
   */
  @ApiOperation("管理员登录接口")
  @PostMapping("/login")
  public Result login(@RequestBody AdminLoginFormDto adminLoginFormDTO) {
    return adminService.login(adminLoginFormDTO);
  }

  /**
   * 管理员查看商户的申请列表.
   *
   * @return 返回信息
   */
  @ApiOperation("查看商户申请列表")
  @GetMapping("/apply-list")
  public Result checkApplyList(@RequestParam Integer userId) {
    //查询所有申请列表
    if (userId == 0) {
      List<Verification> verifications = verificationService.lambdaQuery().list();
      return Result.success(verifications);
    }
    //查询某个商户的申请
    List<Verification> verifications = verificationService.lambdaQuery()
        .eq(Verification::getUserId, userId).list();
    return Result.success(verifications);
  }

  /**
   * 管理员处理商户审核.
   *
   * @param verificationId 识别号
   * @param isPassed       是否通过
   * @return 返回信息
   */
  @ApiOperation("处理商户审核")
  @GetMapping("/deal-verify")
  public Result dealVerify(@RequestParam Integer verificationId, @RequestParam Integer isPassed) {
    //根据id查找申请
    Verification verification = verificationService.getById(verificationId);
    //如果审核通过
    if (isPassed == 0) {
      verification.setStatus(1);
      verificationService.updateById(verification);
      Users user = usersService.getById(verification.getUserId());
      user.setIsMerchant(1);
      usersService.updateById(user);
      return Result.success();
    }
    //如果审核不通过
    verification.setStatus(2);
    verificationService.updateById(verification);
    return Result.success();
  }
}
