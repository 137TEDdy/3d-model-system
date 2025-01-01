package com.liushenwuzu.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Needs;
import com.liushenwuzu.domain.po.Satisfaction;
import com.liushenwuzu.service.INeedsService;
import com.liushenwuzu.service.SatisfactionService;
import com.liushenwuzu.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 用户需求控制层.
 */
@Api(tags = "用户需求相关")
@RestController
@RequestMapping("api/user/need")
public class UserNeedController {

  @Autowired
  SatisfactionService satisfactionService;
  @Autowired
  INeedsService needsService;
  @Autowired
  private UserService userService;

  /**
   * 发布需求.
   *
   * @param userId 用户ID
   * @param title  题目
   * @param note   注解
   * @return 执行结果
   */
  @ApiOperation("发布需求")
  @GetMapping("/post")
  public Result postNeed(@RequestParam("userId") int userId,
                         @RequestParam("title") String title,
                         @RequestParam("note") String note) {
    Needs needs = new Needs();
    needs.setNote(note);
    needs.setTitle(title);
    needs.setUserId(userId);
    needs.setCreatedTime(LocalDateTime.now().toString());
    if (Db.save(needs)) {
      return Result.success();
    }
    return Result.error("发布需求失败");
  }

  /**
   * 选择商户需求.
   *
   * @param userId         用户ID
   * @param satisfactionId 需求ID
   * @param choice         选择类型
   * @return 执行信息
   */
  @ApiOperation("选择商户推荐的商品")
  @GetMapping("select-satisfy")
  public Result selectSatis(@RequestParam("userId") int userId,
                            @RequestParam("satisfactionId") int satisfactionId,
                            @RequestParam("choice") int choice) {
    int onshowId = satisfactionService.selectSatis(userId, satisfactionId, choice);
    if(onshowId==-1){
      return Result.error("该满足不存在,请刷新页面");
    }
    if (onshowId == 0) {
      return Result.error("您已作出选择，无需重复选择");
    }
    if (choice == 1) {
      return userService.buyModel(userId, onshowId,-1);
    }
    return Result.success();
  }

  /**
   * 查询需求情况.
   *
   * @param userId 用户ID
   * @return 执行结果
   */
  @ApiOperation("查询需求情况")
  @GetMapping("query")
  public Result queryNeed(@RequestParam("userId") int userId) {
    return Result.success(needsService.queryNeeds(userId));
  }

  /**
   * 查询某需求的满足情况.
   *
   * @param needId 需求ID
   * @return 执行结果
   */
  @ApiOperation("查询某需求的满足情况")
  @GetMapping("all-products")
  public Result queryAllProducts(@RequestParam("needId") int needId) {
    return Result.success(satisfactionService.querySatis(needId));
  }

  /**
   * 修改需求.
   *
   * @param needId 需求ID
   * @param title  题目
   * @param note   注解
   * @return 执行结果
   */
  @ApiOperation("修改需求")
  @GetMapping("modify")
  public Result modifyNeeds(@RequestParam("needId") int needId,
                            @RequestParam("title") String title,
                            @RequestParam("note") String note) {
    needsService.modifyNeeds(needId, note, title);
    return Result.success();
  }

  /**
   * 删除需求.
   *
   * @param needId 需求ID
   * @return 执行结果
   */
  @ApiOperation("删除需求")
  @GetMapping("/delete")
  public Result delete(@RequestParam("needId") int needId) {
    Needs needs=needsService.getById(needId);
    Integer isSatisfied = needs.getIsSatisfied();
    if(isSatisfied==1){
      return Result.error("此需求已经下架,无需重复下架");
    }
    needs.setIsSatisfied(1);
    needsService.updateById(needs);
    List<Satisfaction> list = satisfactionService.lambdaQuery()
        .eq(Satisfaction::getNeedId, needId).list();
    for(Satisfaction satisfaction:list){
      if(satisfaction.getIsChosen()!=1){
        satisfaction.setIsChosen(3);
        satisfactionService.updateById(satisfaction);
      }
    }
    return Result.success();
  }
}
