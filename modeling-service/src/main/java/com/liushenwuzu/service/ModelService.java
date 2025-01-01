package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.dto.SearchModelDto;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.vo.ModelVo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartRequest;


/**
 * 模型service接口
 *
 * @author 陈立坤
 * @date 2023/12/19
 */
public interface ModelService extends IService<Model> {


  /**
   * 查询某用户所有分享模型接口
   *
   * @param userId
   * @return {@link List}<{@link ModelVo}>
   */
  List<ModelVo> selectAllPublished(Integer userId);

  /**
   * 查询模型接口
   *
   * @param searchModelDTO
   * @return {@link Result}
   */
  Result searchForModel(SearchModelDto searchModelDTO);

  /**
   * 创建模型接口
   *
   * @param name
   * @param userId
   * @param info
   * @param request
   * @param httpRequest
   */
  Result createModel(String name, Integer userId, String info, MultipartRequest request,
                     HttpServletRequest httpRequest) throws Exception;

  /**
   * @param modelId
   * @param type
   * @return {@link Result}
   */
  Result publishModel(Integer modelId, Integer type);

  Result createModelSpecially(Integer userId, String name, String info,
                              HttpServletRequest httpRequest);
}
