package com.liushenwuzu.controller;

import com.baomidou.mybatisplus.extension.toolkit.Db;

import com.liushenwuzu.client.ModelClient;
import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.config.MinioConfig;
import com.liushenwuzu.domain.dto.QueryUserFormDto;
import com.liushenwuzu.domain.dto.UpdateUserFormDto;
import com.liushenwuzu.domain.po.Likes;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.domain.vo.ModelVo;
import com.liushenwuzu.service.ILikesService;
import com.liushenwuzu.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Controller层.
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户相关接口")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private ILikesService likesService;
  @Autowired
  private ModelClient modelClient;
  @Autowired
  private MinioConfig minioConfig;

  /**
   * 根据昵称、id或手机查询用户.
   *
   * @param queryUserFormDTO 查询表单
   * @return 执行信息
   */
  @ApiOperation("查询用户")
  @PostMapping("/query")
  public Result query(@RequestBody QueryUserFormDto queryUserFormDTO) {
    List<User> users = userService.lambdaQuery()
        .like(!"".equals(queryUserFormDTO.getNickname()), User::getNickname,
            queryUserFormDTO.getNickname())
        .eq(queryUserFormDTO.getUserId() != 0, User::getUserId, queryUserFormDTO.getUserId())
        .eq(!"".equals(queryUserFormDTO.getPhone()), User::getPhone, queryUserFormDTO.getPhone())
        .list();
    return Result.success(users);
  }

  /**
   * 根据uerId查询该用户的收藏列表.
   *
   * @param userId 用户ID
   * @return 执行信息
   */
  @GetMapping("/likes")
  @ApiOperation("查询收藏列表")
  public Result likes(@RequestParam Integer userId) {
    //查询该用户所有的收藏
    List<Likes> likes = likesService.lambdaQuery().eq(Likes::getUserId, userId).list();
    //得到模型id列表
    List<Integer> ids = likes.stream().map(Likes::getModelId).collect(Collectors.toList());
    if (ids.isEmpty()) {
      return Result.success();
    }
    //根据模型id列表得到模型列表，调用模型服务
    List<Model> models = modelClient.findModelsByIds(ids);
    List<ModelVo> modelVos = new ArrayList<>(models.size());
    //将所有模型封装在modelVO中
    for (Model model : models) {
      String nickname = Db.getById(model.getUserId(), User.class).getNickname();
      ModelVo modelVO = new ModelVo();
      BeanUtils.copyProperties(model, modelVO);
      modelVO.setNickname(nickname);
      modelVO.setIsLiked(1);
      modelVos.add(modelVO);
    }
    return Result.success(modelVos);
  }

  /**
   * 修改用户信息.
   *
   * @param updateUserFormDTO 修改表单
   * @return 执行信息
   */
  @PostMapping("/update")
  @ApiOperation("修改用户信息")
  public Result update(@RequestBody UpdateUserFormDto updateUserFormDTO) {
    //查找用户
    User user = userService.getById(updateUserFormDTO.getUserId());
    //更新信息
    user.setInfo(updateUserFormDTO.getInfo());
    user.setNickname(updateUserFormDTO.getNickname());
    //跟新数据库
    userService.updateById(user);
    return Result.success();
  }

  /**
   * 修改用户头像.
   *
   * @param userId  用户ID
   * @param request 请求参数
   * @return 执行信息学
   * @throws Exception error
   */
  @PostMapping("update-avatar")
  @ApiOperation("修改用户头像")
  public Result updateAvatar(@RequestParam Integer userId, MultipartRequest request)
      throws Exception {
    //得到请求中的图片文件
    Iterator<String> iterator = request.getFileNames();
    String uploadedFile = "default-avatar";
    if (iterator.hasNext()) {
      uploadedFile = iterator.next();
    }
    MultipartFile multipartFile = request.getFile(uploadedFile);
    //根据userId得到用户
    User user = userService.getById(userId);
    //将用户原本在minio的头像删除,如果是默认头像就不删除
    if (!user.getAvatarUrl().contains("default-avatar")) {
      if (!minioConfig.removeObject(
          user.getAvatarUrl().substring(user.getAvatarUrl().lastIndexOf("/") + 1))) {
        return Result.error("操作失败");
      }
    }
    //将头像存在minio，文件名为上传为userId-avatar
    String path = this.minioConfig.putObject(multipartFile, userId + "-avatar");
    //将头像的信息更新在用户表中
    user.setAvatarUrl(path);
    userService.updateById(user);
    return Result.success();
  }

  /**
   * 根据id查询用户，供modeling-service调用.
   *
   * @param userId 用户ID
   * @return 执行信息
   */
  @GetMapping("/queryById")
  public User queryById(@RequestParam Integer userId) {
    User user = userService.getById(userId);
    return user;
  }

  /**
   * 增加用户生成模型的次数，供modeling-service调用.
   *
   * @param userId 用户ID
   */
  @GetMapping("/add-num")
  public void addNum(@RequestParam Integer userId) {
    User user = userService.getById(userId);
    user.setModelNum(user.getModelNum() + 1);
    userService.updateById(user);
  }
  @GetMapping("/test")
  public Result test(@RequestParam Integer modelId){
    return modelClient.findModelById(modelId);
  }

}
