package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.dto.ChangePasswordFormDto;
import com.liushenwuzu.domain.dto.LoginFormDto;
import com.liushenwuzu.domain.dto.SignUpFormDto;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.vo.UserVo;

/**
 * 用户service实现类
 *
 * @author 陈立坤
 * @date 2023/12/20
 */
public interface UserService extends IService<User> {

  /**
   * 用户登录
   *
   * @param loginFormDTO
   * @return {@link UserVo}
   */
  UserVo login(LoginFormDto loginFormDTO);

  /**
   * 用户注册
   *
   * @param signUpFormDto
   * @return int
   */
  int signUp(SignUpFormDto signUpFormDto);

  /**
   * 用户更改密码
   *
   * @param changePasswordFormDTO
   * @return boolean
   */
  boolean changePassword(ChangePasswordFormDto changePasswordFormDTO);

  /**
   * 忘记密码
   *
   * @param signUpFormDto
   * @return boolean
   */
  boolean forgetPassword(SignUpFormDto signUpFormDto);

  /**
   * 购买该模型
   *
   * @param userId
   * @param onshowId
   * @return {@link Result}
   */
  Result buyModel(int userId, int onshowId,int price);
}
