package com.liushenwuzu.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.dto.ChargeAccountForm;
import com.liushenwuzu.domain.po.TransitionLog;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.po.Verification;
import com.liushenwuzu.service.ITransitionLogService;
import com.liushenwuzu.service.IVerificationService;
import com.liushenwuzu.service.SalesLogService;
import com.liushenwuzu.service.UserService;

import com.liushenwuzu.service.impl.VerificationServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;

import java.util.List;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户交易Controller层.
 */
@RestController
@Api(tags = "用户交易相关接口")
@RequestMapping("/api/user")
public class UserPurchaseController {

  @Autowired
  private UserService userService;
  @Autowired
  private SalesLogService salesLogService;
  @Autowired
  private ITransitionLogService transitionLogService;
  @Autowired
  private IVerificationService verificationService;
  /**
   * 用户申请商户.
   *
   * @param userId 用户ID
   * @param note   注解
   * @return 执行结果
   */
  @ApiOperation("申请商户")
  @GetMapping("/post-verify")
  public Result applyForMerchant(@RequestParam Integer userId, @RequestParam String note) {
    //如果之前已经申请，且还没有被处理
    List<Verification> list = verificationService.lambdaQuery().eq(Verification::getUserId, userId)
        .eq(Verification::getStatus, 0).list();
    Integer isMerchant = userService.getById(userId).getIsMerchant();
    if(isMerchant==1){
      return Result.error("您已经是商户，无需重复申请");
    }
    if(!list.isEmpty()){
      return Result.error("您已经提出申请，正在审核，请勿重复提交");
    }
    Verification verification = new Verification();
    verification.setNote(note);
    verification.setUserId(userId);
    verification.setCreatedTime(LocalDateTime.now().toString());
    boolean save = Db.save(verification);
    if (!save) {
      return Result.error("申请失败");
    }
    return Result.success();
  }

  /**
   * 查看用户是否为商户.
   *
   * @param userId 用户ID
   * @return 执行结果
   */
  @ApiOperation("查看用户是否为商户")
  @GetMapping("/is-merchant")
  public Result checkIfMerchant(@RequestParam Integer userId) {
    User user = userService.getById(userId);
    if (user == null) {
      return Result.error("用户不存在");
    }
    return Result.success(user.getIsMerchant());
  }

  /**
   * 升级为vip.
   *
   * @param userId 用户ID
   * @return 执行结果
   */
  @ApiOperation("升级为vip")
  @GetMapping("/vip")
  public Result getVip(@RequestParam Integer userId) {
    User user = userService.getById(userId);
    if (user.getIsVip() == 1) {
      return Result.error("您已经是vip,无需充值");
    }
    int account = user.getAccount();
    if (account < 15) {
      return Result.error("余额不足，请先充值");
    }
    user.setAccount(account - 15);
    user.setIsVip(1);
    TransitionLog transitionLog=new TransitionLog();
    transitionLog.setAmount(15);
    transitionLog.setType(0);
    transitionLog.setStatus(1);
    transitionLog.setCreatedTime(LocalDateTime.now().toString());
    transitionLog.setUserId(userId);
    transitionLogService.save(transitionLog);
    userService.updateById(user);
    return Result.success();
  }

  /**
   * 购买模型.
   *
   * @param userId   用户ID
   * @param onshowId 模型ID
   * @return 执行结果
   */
  @ApiOperation("购买模型")
  @GetMapping("/buy-model")
  public Result buyModel(@RequestParam("userId") Integer userId,
      @RequestParam("onshowId") int onshowId,@RequestParam("price") Integer price) {
    return userService.buyModel(userId, onshowId,price);
  }

  /**
   * 查询购买列表.
   *
   * @param userId 用户ID
   * @return 执行结果
   */
  @ApiOperation("查询购买列表")
  @GetMapping("bought-models")
  public Result boughtModels(@RequestParam("userId") int userId) {
    return Result.success(salesLogService.queryUserBought(userId));
  }

  /**
   * 余额充值.
   *
   * @param chargeAccountForm 查询表单
   * @return 执行结果
   */
  @ApiOperation("余额充值")
  @PostMapping("/account-charge")
  public Result accountCharge(@RequestBody ChargeAccountForm chargeAccountForm) {
    User user = userService.getById(chargeAccountForm.getUserId());
    if (user == null) {
      return Result.error("用户不存在！");
    }
    user.setAccount(user.getAccount() + chargeAccountForm.getNum());
    userService.updateById(user);
    TransitionLog transitionLog=new TransitionLog();
    transitionLog.setAmount(chargeAccountForm.getNum());
    transitionLog.setType(1);
    transitionLog.setStatus(1);
    transitionLog.setCreatedTime(LocalDateTime.now().toString());
    transitionLog.setUserId(chargeAccountForm.getUserId());
    transitionLogService.save(transitionLog);
    return Result.success();
  }

  /**
   * 查看个人余额.
   *
   * @param userId 用户ID
   * @return 执行结果
   */
  @ApiOperation("查看个人余额")
  @GetMapping("check-amount")
  public Result checkAmount(@RequestParam("userId") int userId) {
    User user = userService.getById(userId);
    if (user == null) {
      return Result.error("用户无效！！");
    }
    return Result.success(user.getAccount());
  }
}
