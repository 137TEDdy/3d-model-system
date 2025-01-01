package com.liushenwuzu.client;

import com.liushenwuzu.common.pojo.Result;
import com.liushenwuzu.domain.po.Model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 模型服务代理接口，可以用此接口调用模型服务.
 */
@FeignClient("model-service")
public interface ModelClient {

  /**
   * @param ids
   * @return {@link List}<{@link Model}>
   */
  @GetMapping("/api/model/findModelsByIds")
  List<Model> findModelsByIds(@RequestParam List<Integer> ids);
  @GetMapping("/api/model/query-one")
  Result findModelById(@RequestParam Integer modelId);
}

