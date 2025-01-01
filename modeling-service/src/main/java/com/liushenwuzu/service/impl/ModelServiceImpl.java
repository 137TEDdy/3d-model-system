package com.liushenwuzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;

import com.liushenwuzu.client.UserClient;
import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.config.MinioConfig;
import com.liushenwuzu.domain.dto.SearchModelDto;
import com.liushenwuzu.domain.po.Image;
import com.liushenwuzu.domain.po.User;
import com.liushenwuzu.pipeline.ProductApi;
import com.liushenwuzu.service.ModelService;
import com.liushenwuzu.domain.po.Like;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.Share;
import com.liushenwuzu.domain.vo.ModelVo;
import com.liushenwuzu.mapper.ModelMapper;
import com.liushenwuzu.service.ShareService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 * 模型服务
 *
 * @author 陈立坤
 * @date 2023/12/20
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

  @Autowired
  private ShareService shareService;
  @Autowired
  private MinioConfig minioConfig;
  @Autowired
  private UserClient userClient;

  /**
   * @param userId
   * @return {@link List}<{@link ModelVo}>
   */
  @Override
  public List<ModelVo> selectAllPublished(Integer userId) {
    //找到所有已经分享的模型
    List<Share> shares = Db.lambdaQuery(Share.class).list();
    List<ModelVo> modelVos = new ArrayList<>(shares.size());
    //遍历所有已经分享的模型
    for (Share share : shares) {
      Model model = getById(share.getModelId());

      //如果模型被锁定就不返回
      if (model==null||model.getStatus() != 0) {
        continue;
      }
      ModelVo modelVO = new ModelVo();
      BeanUtils.copyProperties(model, modelVO);
      modelVO.setIsLiked(0);
      User user = Db.lambdaQuery(User.class)
          .eq(User::getUserId, model.getUserId()).one();
      if (user == null) {
        continue;
      }
      modelVO.setNickname(user.getNickname());
      modelVos.add(modelVO);
    }
    //设置用户是否收藏该模型
    List<Like> likes = Db.lambdaQuery(Like.class)
        .eq(Like::getUserId, userId)
        .list();
    List<Integer> likedModelIds = likes.stream().map(Like::getModelId).collect(Collectors.toList());
    for (ModelVo modelVO : modelVos) {
      if (likedModelIds.contains(modelVO.getModelId())) {
        modelVO.setIsLiked(1);
      }
    }
    return modelVos;
  }

  /**
   * 查询模型
   *
   * @param searchModelDTO
   * @return
   */
  @Override
  public Result searchForModel(SearchModelDto searchModelDTO) {
    List<Model> models = this.search(searchModelDTO);
    //返回给前端的查询结果
    List<ModelVo> modelVos = new ArrayList<>();
    //遍历所有查找出来的模型
    for (Model model : models) {
      ModelVo modelVO = new ModelVo();
      //设置nickname
      modelVO.setNickname(Db.getById(model.getUserId(), User.class).getNickname());
      //查找并设置是否收藏
      List<Like> likes = Db.lambdaQuery(Like.class)
          .eq(Like::getModelId, model.getModelId())
          .eq(Like::getUserId, model.getUserId()).list();
      if (!likes.isEmpty()) {
        modelVO.setIsLiked(1);
      } else {
        modelVO.setIsLiked(0);
      }
      //将查找结果赋值给VO
      BeanUtils.copyProperties(model, modelVO);
      modelVO.setIsShared(shareService.isShared(model.getModelId()));
      modelVos.add(modelVO);
    }
    return Result.success(modelVos);
  }

  /**
   * 用图片创建模型
   *
   * @param name
   * @param userId
   * @param info
   * @param request
   * @param httpRequest
   * @return {@link Result}
   * @throws Exception
   */
  @Override
  public Result createModel(String name, Integer userId, String info, MultipartRequest request,
                            HttpServletRequest httpRequest) throws Exception {
    if (!ProductApi.haveSpareQueue()) {
      return Result.error("当前任务队列已满");
    }
    User user = Db.getById(userId, User.class);
    if (user.getIsVip() == 0 && user.getModelNum() >= 10) {
      return Result.error("非vip用户最多只能生成10次模型,请升级为vip以获得更好体验");
    }
    Model model = this.addModel(userId, info, name);
    //遍历request中的照片文件
    Iterator<String> iterator = request.getFileNames();
    //将照片暂存本地，获取照片目录的绝对路径
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String time = sdf.format(new Date());
    String runPath = httpRequest.getServletContext().getRealPath("/temp_" + userId + '_' + time);
    File tmp = new File(runPath);
    if (!tmp.exists()) {
      tmp.mkdir();
    }
    String coverPath = this.addImages(iterator, request, model, userId, runPath);
    model.setStatus(2);//生成中
    model.setCoverUrl(coverPath);
    //在数据库中存一个新的模型
    Db.updateById(model);
    ProductApi.noteToProduct(userId, time, runPath, model.getModelId());
    return Result.success();
  }

  /**
   * 发布或取消发布模型
   *
   * @param modelId
   * @param type
   * @return {@link Result}
   */
  @Override
  public Result publishModel(Integer modelId, Integer type) {
    //根据模型id得到模型
    Model model = this.getById(modelId);
    //如果模型不存在
    if (model == null) {
      return Result.error("模型不存在");
    }
    //查找此模型是否已经发布
    Share share1 = shareService.lambdaQuery()
        .eq(Share::getModelId, modelId)
        .eq(Share::getUserId, model.getUserId()).one();
    if (type == 0) {//发布模型
      return doPublisModel(model, share1, modelId);
    }
    //取消发布
    //如果没有发布过该模型
    if (share1 == null) {
      return Result.error("您还未发布过该模型");
    }
    //发布过该模型，要取消发布，删除数据
    shareService.removeById(share1);
    return Result.success();
  }

  @Override
  public Result createModelSpecially(Integer userId, String name, String info,
                                     HttpServletRequest httpRequest) {
    User user = Db.getById(userId, User.class);
    if (user.getIsVip() == 0 && user.getModelNum() >= 10) {
      return Result.error("非vip用户最多只能生成10次模型,请升级为vip以获得更好体验");
    }
    Model model = this.addModel(userId, info, name);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String time = sdf.format(new Date());
    String runPath = httpRequest.getServletContext().getRealPath("/temp_" + userId + '_' + time);
    File tmp = new File(runPath);
    tmp.mkdir();
    String coverPath = "http://106.15.39.106:9001/easy-model/1-12-photo0";
    model.setStatus(2);//生成中
    model.setCoverUrl(coverPath);
    //在数据库中存一个新的模型
    Db.updateById(model);
    ProductApi.noteToProductSpecially(userId, time, runPath, model.getModelId());
    return Result.success();
  }

  /**
   * 发布模型
   *
   * @param model
   * @param share1
   * @param modelId
   * @return {@link Result}
   */
  private Result doPublisModel(Model model, Share share1, Integer modelId) {
    //发布模型

    if (model.getStatus() == 3) {
      return Result.error("模型已经被禁用了,不能分享");
    }
    //如果已经发布过该模型
    if (share1 != null) {
      return Result.error("您已经分享过该模型了");
    }
    //没有发布过该模型
    Share share = new Share();
    share.setUserId(model.getUserId());
    share.setModelId(modelId);
    share.setCreatedTime(LocalDateTime.now().toString());
    shareService.save(share);
    return Result.success();
  }

  /**
   * 在minio中添加图片
   *
   * @param iterator
   * @param request
   * @param model
   * @param userId
   * @param runPath
   * @return 封面图片地址
   * @throws Exception
   */
  private String addImages(Iterator<String> iterator, MultipartRequest request, Model model,
                           Integer userId, String runPath)
      throws Exception {
    int i = 0;
    String coverPath = "";
    while (iterator.hasNext()) {
      String uploadedFile = iterator.next();
      MultipartFile multipartFile = request.getFile(uploadedFile);
      //将照片存在minio，文件名为上传为modelID-userId-photo+num
      String filename = model.getModelId() + "-" + userId + "-photo" + i++;
      String path = this.minioConfig.putObject(multipartFile, filename);
      //将照片的信息存在数据库
      Image image = new Image();
      image.setImagePath(path);
      image.setModelId(model.getModelId());
      image.setUploadTime(LocalDateTime.now().toString());
      if (i == 1) {
        coverPath = path;
      }
      Db.save(image);
      multipartFile.transferTo(new File(runPath, multipartFile.getOriginalFilename()));
    }
    return coverPath;
  }


  /**
   * 在数据库中添加模型
   *
   * @param userId
   * @param info
   * @param name
   * @return {@link Model}
   */
  private Model addModel(Integer userId, String info, String name) {
    //用户生成模型的次数加一
    userClient.addNum(userId);
    //新建一个模型，设置基本信息并保存在数据库
    Model model = new Model();
    model.setInfo(info);
    model.setName(name);
    model.setUserId(userId);
    model.setCreatedTime(LocalDateTime.now().toString());
    Db.save(model);
    return model;
  }


  /**
   * 根据id或关键字或模型名称搜索模型
   *
   * @param searchModelDTO
   * @return
   */
  private List<Model> search(SearchModelDto searchModelDTO) {
    //根据id或关键字或模型名称搜索模型
    List<Model> models = this.lambdaQuery()
        .eq(searchModelDTO.getModelId() != 0, Model::getModelId, searchModelDTO.getModelId())
        .eq(searchModelDTO.getUserId() != 0, Model::getUserId, searchModelDTO.getUserId())
        .and(!"".equals(searchModelDTO.getKeywords()), wrapper -> wrapper
            .like(!"".equals(searchModelDTO.getKeywords()), Model::getInfo,
                searchModelDTO.getKeywords().replace("%", "//%").replace("_", "\\_"))
            .or()
            .like(!"".equals(searchModelDTO.getKeywords()), Model::getName,
                searchModelDTO.getKeywords().replace("%", "//%").replace("_", "\\_"))
        ).list();
    return models;
  }
}
