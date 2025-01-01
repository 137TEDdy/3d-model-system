package com.liushenwuzu.controller;


import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.dto.ChangePasswordFormDto;
import com.liushenwuzu.domain.dto.LoginFormDto;
import com.liushenwuzu.domain.dto.SignUpFormDto;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.vo.UserVo;
import com.liushenwuzu.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录Controller层.
 */
@Api(tags = "登录相关接口")
@RestController
@RequestMapping("/api/user")
public class LoginController {

  @Autowired
  private UserService userService;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  /**
   * 用户登录.
   *
   * @param loginFormDTO 登录表单
   * @return 执行信息
   */
  @ApiOperation("用户登录接口")
  @PostMapping("/login")
  public Result login(@RequestBody LoginFormDto loginFormDTO) {
    //检查电话或者密码是否为空
    if (loginFormDTO.getPhone() == null || loginFormDTO.getPassword() == null) {
      return Result.error("电话号码或密码不能为空");
    }
    //检查电话是否符合格式
    if (!loginFormDTO.getPhone().matches("\\d{11}")) {
      return Result.error("电话号码格式不对");
    }
    //用户登录
    UserVo userVO = userService.login(loginFormDTO);
    //如果电话或密码错误
    if (userVO == null) {
      return Result.error("电话号码或密码错误");
    }
    //如果账号异常
    if (userVO.getStatus() == 1) {
      return Result.error("账号异常，已被管理员禁用");
    }
    return Result.success(userVO);
  }

  /**
   * 获取验证码.
   *
   * @param phone 电话号码
   * @return 执行信息
   */
  @ApiOperation("获取验证码接口")
  @GetMapping("/verify")
  public Result verify(@RequestParam String phone) {
    if (!phone.matches("\\d{11}")) {
      return Result.error("电话号码格式不对");
    }
    //验证码三分钟后过期
    stringRedisTemplate.opsForValue().set(phone, "123456", 3, TimeUnit.MINUTES);
    return Result.success();
  }

  /**
   * 注册新用户.
   *
   * @param signUpFormDto 注册表单
   * @return 执行信息
   */
  @ApiOperation("注册新用户接口")
  @PostMapping("/register")
  public Result signUp(@RequestBody SignUpFormDto signUpFormDto) {
    //检查参数是否缺失
    if (signUpFormDto.getPhone() == null || signUpFormDto.getPassword() == null
        || signUpFormDto.getVerifyCode() == null) {
      return Result.error("参数缺失");
    }
    //检查验证码是否错误
    if (!Objects.equals(signUpFormDto.getVerifyCode(),
        stringRedisTemplate.opsForValue().get(signUpFormDto.getPhone()))) {
      return Result.error("验证码错误");
    }
    //检查手机号是否已经被注册
    int flag = userService.signUp(signUpFormDto);
    if (flag == 0) {
      return Result.error("手机号已经注册");
    }
    return Result.success();
  }

  /**
   * 用户根据旧密码修改密码.
   *
   * @param changePasswordFormDTO 修改表单
   * @return 执行信息
   */
  @ApiOperation("修改密码")
  @PostMapping("/modify-passwd")
  public Result modifyPassword(@RequestBody ChangePasswordFormDto changePasswordFormDTO) {
    //检查新旧密码是否相同
    if (Objects.equals(changePasswordFormDTO.getOldPassword(),
        changePasswordFormDTO.getNewPassword())) {
      return Result.error("新密码不能和旧密码相同");
    }
    //旧密码正确，修改成功
    if (userService.changePassword(changePasswordFormDTO)) {
      return Result.success();
    }
    //旧密码错误
    return Result.error("密码错误,请重新输入");
  }

  /**
   * 忘记密码,用手机验证码找回密码.
   *
   * @param signUpFormDto 修改表单
   * @return 执行信息
   */
  @ApiOperation("忘记密码接口")
  @PostMapping("/forget-passwd")
  public Result forgetPassword(@RequestBody SignUpFormDto signUpFormDto) {
    //检查验证码是否正确
    if (!Objects.equals(signUpFormDto.getVerifyCode(),
        stringRedisTemplate.opsForValue().get(signUpFormDto.getPhone()))) {
      return Result.error("验证码错误");
    }
    //修改密码为新密码
    if (userService.forgetPassword(signUpFormDto)) {
      return Result.success();
    }
    //手机号错误
    return Result.error("手机号错误");
  }

  /**
   * 管理员启用或禁用用户.
   */
  @ApiOperation("启用或禁用用户")
  @GetMapping("/status")
  public Result status(@RequestParam String userId, @RequestParam Integer status) {
    User user = userService.getById(userId);
    user.setStatus(status);
    userService.updateById(user);
    return Result.success();
  }

}
