package com.liushenwuzu.service;

import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.OnShow;

import com.baomidou.mybatisplus.extension.service.IService;



/**
 * 在售模型service层接口
 *
 * @author 陈立坤
 * @date 2023/12/20
 */
public interface OnShowService extends IService<OnShow> {

  /**
   * 编辑在售模型
   *
   * @param onshowId
   * @return {@link Model}
   */
  Model findModel(int onshowId);

}
