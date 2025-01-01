package com.liushenwuzu.controller;

import com.liushenwuzu.client.UserClient;
import com.liushenwuzu.domain.dto.SearchModelDto;
import com.liushenwuzu.domain.po.Like;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.vo.ModelVo;
import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.service.IUsersService;
import com.liushenwuzu.service.LikeService;
import com.liushenwuzu.service.ModelService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

@Api(tags = "模型相关接口")
@RestController
@RequestMapping("/api/model")
public class ModelController {

  @Autowired
  private ModelService modelService;
  @Autowired
  private LikeService likeService;
  @Autowired
  private IUsersService usersService;

  @Autowired
  private UserClient userClient;
  /**
   * 根据模型id或用户id或关键词来查找模型 管理员使用的接口，不管模型是否正常生成，是否发布，都能查找到
   *
   * @param searchModelDTO
   * @return
   */
  @ApiOperation("查询模型接口")
  @PostMapping("/search")
  public Result searchForModel(@RequestBody SearchModelDto searchModelDTO) {
    return modelService.searchForModel(searchModelDTO);
  }

  /**
   * 某用户查询已经发布的模型
   *
   * @param userId
   * @return
   */
  @GetMapping("/published")
  @ApiOperation("某用户查询所有已经发布的模型")
  public Result published(@RequestParam Integer userId) {
    List<ModelVo> list = modelService.selectAllPublished(userId);
    return Result.success(list);
  }

  /**
   * 根据userId查询某用户已经发布的模型
   *
   * @param userId
   * @return
   */
  @GetMapping("/own-published")
  @ApiOperation("查询某用户自己已经发布的模型")
  public Result ownPublished(@RequestParam Integer userId) {
    return usersService.ownPublished(userId);
  }


  /**
   * 上传图片生成模型
   *
   * @param name
   * @param userId
   * @param info
   * @param request
   * @param httpRequest
   * @return
   * @throws Exception
   */
  @ApiOperation("用图片创建模型接口")
  @PostMapping("/create/image")
  public Result createModelByImages(@RequestParam Integer num, @RequestParam String name,
                                    @RequestParam Integer userId, @RequestParam String info,
                                    MultipartRequest request, HttpServletRequest httpRequest) throws Exception {
    return modelService.createModel(name, userId, info, request, httpRequest);
  }

  @ApiOperation("用图片创建模型接口special")
  @GetMapping("/create/image_special")
  public Result createModelByImagesSpecially(@RequestParam Integer userId,
                                             @RequestParam String name,
                                             @RequestParam String info,
                                             HttpServletRequest httpRequest) {
    return modelService.createModelSpecially(userId, name, info, httpRequest);
  }

  /**
   * 根据模型id删除模型
   *
   * @param modelId
   * @return
   * @throws Exception
   */
  @GetMapping("/delete")
  @ApiOperation(value = "根据模型ID删除模型")
  public Result deleteModel(@RequestParam Integer modelId) {
    modelService.removeById(modelId);
    return Result.success();
  }

  /**
   * 发布或取消发布模型
   *
   * @param modelId
   * @param type
   * @return
   */
  @GetMapping("/publish")
  @ApiOperation(value = "(取消)发布模型接口")
  public Result publishModel(@RequestParam Integer modelId, @RequestParam Integer type) {
    return modelService.publishModel(modelId, type);
  }

  /**
   * 收藏或取消收藏模型
   *
   * @param userId
   * @param modelId
   * @param type
   * @return
   */
  @GetMapping("/modify-like")
  @ApiOperation(value = "（取消）收藏模型")
  public Result changeLike(@RequestParam Integer userId, @RequestParam Integer modelId,
      @RequestParam Integer type) {
    //取消收藏模型
    if (type == 1) {
      Like like = likeService.lambdaQuery()
          .eq(Like::getUserId, userId)
          .eq(Like::getModelId, modelId).one();
      //如果收藏过该模型则取消收藏否则不做处理
      if (like != null) {
        likeService.removeById(like);
      }
      return Result.success();
    }
    //收藏模型
    Like like = likeService.lambdaQuery()
        .eq(Like::getUserId, userId)
        .eq(Like::getModelId, modelId).one();
    //已经收藏了该模型则直接返回不用做处理
    if (like != null) {
      return Result.success();
    }
    //还没有收藏该模型
    Like like1 = new Like();
    like1.setModelId(modelId);
    like1.setCreatedTime(LocalDateTime.now().toString());
    like1.setUserId(userId);
    likeService.saveOrUpdate(like1);
    return Result.success();
  }

  /**
   * 根据id修改模型信息或模型名称
   *
   * @param modelId
   * @param name
   * @param info
   * @return
   */
  @GetMapping("/modify-info")
  @ApiOperation("修改模型信息")
  public Result modifyModelInfo(@RequestParam Integer modelId, @RequestParam String name,
      @RequestParam String info) {
    Model model = modelService.getById(modelId);
    if(model==null){
      return Result.error("模型不存在,请刷新页面");
    }
    model.setInfo(info);
    model.setName(name);
    modelService.updateById(model);
    return Result.success();
  }

  /**
   * 根据模型ID修改模型状态
   *
   * @param modelId
   * @param type
   * @return
   */
  @GetMapping("modify-status")
  @ApiOperation("修改模型锁定状态")
  public Result modifyModelStatus(@RequestParam Integer modelId, @RequestParam Integer type) {
    Model model = modelService.getById(modelId);
    if(model==null){
      return Result.error("模型不存在,请刷新页面");
    }
    //禁用模型
    if (type == 0) {
      model.setStatus(3);
    }
    //启用模型
    else {
      model.setStatus(0);
    }
    modelService.updateById(model);
    return Result.success();
  }

  /**
   * 根据模型ID列表得到模型列表
   *
   * @param ids
   * @return
   */
  @GetMapping("findModelsByIds")
  public List<Model> findModelsByIds(@RequestParam List<Integer> ids) {
    List<Model> models=new ArrayList<>();
    for(Integer id:ids){
      Model model = modelService.getById(id);
      if(model==null){
        continue;
      }
      models.add(model);
    }
    return models;
  }

  /**
   * 根据模型id查找一个模型
   *
   * @param modelId
   * @return
   */
  @GetMapping("/query-one")
  @ApiOperation("根据id查找一个模型")
  public Result findModelById(@RequestParam Integer modelId) {
    //查找模型
    Model model = modelService.getById(modelId);
    if(model==null){
      return Result.error("模型不存在");
    }
    ModelVo modelVO = new ModelVo();
    BeanUtils.copyProperties(model, modelVO);
    //设置模型创作者nickname
    modelVO.setNickname(usersService.getById(model.getUserId()).getNickname());
    modelVO.setIsLiked(0);
    return Result.success(modelVO);
  }

  @GetMapping("/test")
  public Result test(@RequestParam Integer userId){
    User user1 = userClient.queryUserById(userId);
    System.out.println(user1.getModelNum());
    userClient.addNum(userId);
    User user2 = userClient.queryUserById(userId);
    System.out.println(user2.getModelNum());
    return Result.success(""+user1.getModelNum()+"->"+user2.getModelNum());
  }
}
