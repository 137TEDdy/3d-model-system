package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.dto.ChangePasswordFormDto;
import com.liushenwuzu.domain.dto.LoginFormDto;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.domain.po.SalesLog;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.po.UserLog;
import com.liushenwuzu.domain.vo.UserVo;
import com.liushenwuzu.mapper.ModelMapper;
import com.liushenwuzu.mapper.SalesLogMapper;
import com.liushenwuzu.service.ModelService;
import com.liushenwuzu.service.OnShowService;
import com.liushenwuzu.service.SalesLogService;
import com.liushenwuzu.domain.dto.SignUpFormDto;
import com.liushenwuzu.mapper.OnShowMapper;
import com.liushenwuzu.mapper.UserMapper;
import com.liushenwuzu.service.UserService;
import com.liushenwuzu.common.utils.JwtUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户service层
 *
 * @author 陈立坤
 * @date 2023/12/20
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Autowired
  UserMapper userMapper;
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  OnShowMapper onShowMapper;
  @Autowired
  SalesLogMapper salesLogMapper;
  @Autowired
  SalesLogService salesLogService;
  @Autowired
  ModelService modelService;
  @Autowired
  OnShowService onShowService;

  /**
   * 用户登录
   *
   * @param loginFormDTO
   * @return {@link UserVo}
   */
  @Override
  public UserVo login(LoginFormDto loginFormDTO) {
    // 根据手机号查询
    User user = lambdaQuery().eq(User::getPhone, loginFormDTO.getPhone()).one();
    if (user == null) {
      return null;
    }
    UserLog userLog = new UserLog();
    userLog.setIp(loginFormDTO.getIp());
    userLog.setUserId(user.getUserId());
    userLog.setLogTime(LocalDateTime.now().toString());
    if (!loginFormDTO.getPassword().equals(user.getPassword())) {
      userLog.setStatus(0);
      Db.save(userLog);
      return null;
    }
    userLog.setStatus(1);
    Db.save(userLog);
    UserVo userVO = new UserVo();
    BeanUtils.copyProperties(user, userVO);
    // 5.生成TOKEN
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", userVO.getUserId());
    claims.put("username", userVO.getNickname());
    String token = JwtUtils.generateJwt(claims);
    userVO.setToken(token);
    return userVO;
  }

  /**
   * 用户注册
   *
   * @param signUpFormDto
   * @return int
   */
  @Override
  public int signUp(SignUpFormDto signUpFormDto) {
    QueryWrapper<User> wrapper = new QueryWrapper<User>()
        .select("user_id")
        .eq("phone", signUpFormDto.getPhone());
    if (userMapper.selectOne(wrapper) != null) {
      return 0;//手机号已经被注册了
    }
    User user = new User();
    BeanUtils.copyProperties(signUpFormDto, user);
    user.setRegisterTime(LocalDateTime.now().toString());
    user.setAvatarUrl("http://106.15.39.106:9001/easy-model/default-avatar.png");
    save(user);
    user.setNickname("用户" + user.getUserId());
    updateById(user);
    return 1;
  }

  /**
   * 改密码
   *
   * @param changePasswordFormDTO
   * @return boolean
   */
  @Override
  public boolean changePassword(ChangePasswordFormDto changePasswordFormDTO) {
    QueryWrapper<User> wrapper = new QueryWrapper<User>()
        .select("user_id")
        .eq("phone", changePasswordFormDTO.getPhone())
        .eq("password", changePasswordFormDTO.getOldPassword());
    User user = userMapper.selectOne(wrapper);
    if (user == null) {
      return false;
    }
    user.setPassword(changePasswordFormDTO.getNewPassword());
    updateById(user);
    return true;
  }

  /**
   * 忘记密码
   *
   * @param signUpFormDto
   * @return boolean
   */
  @Override
  public boolean forgetPassword(SignUpFormDto signUpFormDto) {
    QueryWrapper<User> wrapper = new QueryWrapper<User>()
        .select("user_id")
        .eq("phone", signUpFormDto.getPhone());
    User user = userMapper.selectOne(wrapper);
    if (user == null) {
      return false;
    }
    user.setPassword(signUpFormDto.getPassword());
    updateById(user);
    return true;
  }

  /**
   * 购买模型
   *
   * @param userId
   * @param onshowId
   * @return {@link Result}
   */
  @Override
  public Result buyModel(int userId, int onshowId,int price) {
    OnShow onShow = onShowMapper.selectById(onshowId);
    Model model = modelMapper.selectById(onShow.getModelId());
    User user = userMapper.selectById(userId);
    if (onShow == null) {
      return Result.error("该商品不存在，请刷新页面");
    }
    if(price!=-1&&onShow.getPrice()!=price){
      return Result.error("价格不一致，请刷新页面");
    }
    if (onShow.getFlag() == 0) {
      return Result.error("该模型已经下架，不能购买");
    }
    if (user.getUserId().equals(model.getUserId())) {
      return Result.error("这是自己的模型，不能购买");
    }
    if (user.getAccount() < onShow.getPrice()) {
      return Result.error("余额不足");
    }
    List<SalesLog> list = salesLogService.lambdaQuery()
        .eq(SalesLog::getModelId, onShow.getModelId()).eq(SalesLog::getTargetUserId, userId).list();
    if (!list.isEmpty()) {
      return Result.error("您已经购买过该模型了，无需重复购买");
    }
    onShow.setNum(onShow.getNum() + 1);
    onShowMapper.updateById(onShow);
    user.setAccount(user.getAccount() - onShow.getPrice());
    userMapper.updateById(user);
    User merchant = userMapper.selectById(onShow.getUserId());
    merchant.setAccount(merchant.getAccount() + onShow.getPrice());
    userMapper.updateById(merchant);
    SalesLog salesLog = new SalesLog(onShow.getModelId(), onShow.getUserId(), userId,
        LocalDateTime.now().toString(), 0, onShow.getPrice());
    salesLogService.saveSaleLog(salesLog);
    model.setUserId(userId);
    model.setModelId(null);
    model.setIsBought(1);
    modelService.saveModel(model);
    return Result.success();
  }
}
