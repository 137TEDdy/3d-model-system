package com.liushenwuzu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Model;
import com.liushenwuzu.domain.po.OnShow;
import com.liushenwuzu.domain.vo.ViewOnShowVo;

import java.util.List;

/**
 * 商品接口
 */
public interface OnShowService extends IService<OnShow> {

  /**
   * 发布商品
   *
   * @param userId  用户id
   * @param modelId 模型id
   * @param price   价格
   * @param note    备注
   * @return 成功/失败
   */
  Result publishOnShow(int userId, int modelId, int price, String note);

  /**
   * 商户商品列表
   *
   * @param userId 用户id
   * @return 商品列表
   */
  List<ViewOnShowVo> viewOnShow(int userId);

  /**
   * 通过商品id查找model
   *
   * @param onshowId 商品id
   * @return 模型信息
   */
  Model findModel(int onshowId);

  /**
   * 修改商品信息
   *
   * @param onSaleId 商品id
   * @param price    价格
   * @param note     备注
   * @return 成功/失败
   */
  boolean modifyOnSale(int onSaleId, int price, String note);
}
