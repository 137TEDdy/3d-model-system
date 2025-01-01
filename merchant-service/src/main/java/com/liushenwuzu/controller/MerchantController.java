package com.liushenwuzu.controller;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.domain.po.Satisfaction;
import com.liushenwuzu.domain.vo.MerchantVo;
import com.liushenwuzu.domain.vo.QuerySatisVo;
import com.liushenwuzu.domain.vo.ViewOnShowVo;
import com.liushenwuzu.service.MerchantService;
import com.liushenwuzu.service.OnShowService;
import com.liushenwuzu.service.SalesLogService;
import com.liushenwuzu.service.SatisfactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商户的控制层
 */
@Api(tags = "商户相关接口")
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

  @Autowired
  MerchantService merchantService;

  @Autowired
  OnShowService onShowService;

  @Autowired
  SatisfactionService satisfactionService;

  @Autowired
  SalesLogService salesLogService;

  /**
   * 商户登录接口
   *
   * @param phone    电话
   * @param password 密码
   * @return 成功/失败信息
   */
  @ApiOperation("商户登录接口")
  @GetMapping("/login")
  public Result login(@RequestParam("phone") String phone,
      @RequestParam("password") String password) {
    return merchantService.login(phone,password);
  }

  /**
   * 商户发布商品接口
   *
   * @param userId  用户Id
   * @param modelId 模型Id
   * @param price   价格
   * @param note    备注
   * @return 成功/失败信息
   */
  @ApiOperation("商户发布商品")
  @GetMapping("/publish")
  public Result publish(@RequestParam("userId") int userId, @RequestParam("modelId") int modelId,
      @RequestParam("price") int price, @RequestParam("note") String note) {
    return onShowService.publishOnShow(userId, modelId, price, note);
  }

  /**
   * 查看发布了的商品
   *
   * @param userId 用户Id
   * @return 商品列表
   */
  @ApiOperation("查看发布了的商品")
  @GetMapping("/goods-list")
  public Result listGoods(@RequestParam("userId") int userId) {
    List<ViewOnShowVo> viewOnShowVoList = onShowService.viewOnShow(userId);
    if (viewOnShowVoList.isEmpty()) {
      return Result.error("没发售信息！！");
    }
    return Result.success(viewOnShowVoList);
  }

  /**
   * 商户下架商品
   *
   * @param userId   用户Id
   * @param onshowId 商品Id
   * @return 成功/失败信息
   */
  @ApiOperation("商户下架商品")
  @GetMapping("/delete-good")
  public Result deleteGoods(@RequestParam("userId") int userId,
      @RequestParam("onshowId") int onshowId) {
    OnShow onShow = onShowService.getById(onshowId);
    if(null==onShow){
      return Result.error("该商品没有上架");
    }
    if(onShow.getUserId()!=userId){
      return Result.error("此商品不属于你，不能下架");
    }
    Integer flag= onShow.getFlag();
    if(flag==0){
      return Result.error("商品已经下架，无需重复下架");
    }
    Satisfaction satisfaction = satisfactionService.lambdaQuery()
        .eq(Satisfaction::getOnSaleId, onshowId)
        .eq(Satisfaction::getIsChosen, 0).one();
    satisfactionService.removeById(satisfaction);
    onShow.setFlag(0);
    onShowService.updateById(onShow);
    return Result.success();
  }

  /**
   * 商户提供需求
   *
   * @param userId   用户Id
   * @param needId   需求Id
   * @param onshowId 商品Id
   * @return 成功/失败信息
   */
  @ApiOperation("提供满足")
  @GetMapping("/offer-satisfy")
  public Result offerSatisfy(@RequestParam("userId") int userId, @RequestParam("needId") int needId,
      @RequestParam("onshowId") int onshowId) {
    return satisfactionService.addSatis(userId, needId, onshowId);
  }

  /**
   * 查询商户的所有需求
   *
   * @param userId 用户Id
   * @return 需求列表
   */
  @ApiOperation("查询商户提供的所有满足")
  @GetMapping("/query-own-satisfy")
  public Result querySatisfy(@RequestParam("userId") int userId) {
    List<QuerySatisVo> querySatisVoList = satisfactionService.querySatis(userId);
    if (querySatisVoList.size() == 0) {
      return Result.error("没有该项！");
    } else {
      return Result.success(querySatisVoList);
    }
  }

  /**
   * 查看个人收入情况
   *
   * @param userId 用户Id
   * @return 个人收入列表
   */
  @ApiOperation("查看个人收入情况")
  @GetMapping("/cash-flow")
  public Result cashFlow(@RequestParam("userId") int userId) {
    return Result.success(salesLogService.cashFlowList(userId));
  }

  /**
   * 修改商品信息
   *
   * @param onshowId 商品Id
   * @param price    价格
   * @param note     备注
   * @return 成功/失败信息
   */
  @ApiOperation("修改商品信息")
  @GetMapping("/modify-goods")
  public Result modifyGoods(@RequestParam("onshowId") int onshowId,
      @RequestParam("price") int price, @RequestParam("note") String note) {
    if (onShowService.modifyOnSale(onshowId, price, note)) {
      return Result.success();
    }
    return Result.error("该商品不存在！！");
  }

  @ApiOperation("查询某件商品")
  @GetMapping("/target_good")
  public Result findGoods(@RequestParam int onshowId){
    OnShow onShow = onShowService.getById(onshowId);
    if(onShow==null){
      return Result.error("该商品不存在");
    }
    if(onShow.getFlag()==0){
      return Result.error("该商品已经下架了");
    }
    return Result.success(onShow);
  }
}
