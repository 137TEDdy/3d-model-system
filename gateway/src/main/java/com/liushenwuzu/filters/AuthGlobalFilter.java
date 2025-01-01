package com.liushenwuzu.filters;

import com.liushenwuzu.config.AuthProperties;
import com.liushenwuzu.common.utils.JwtUtils;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 全局拦截器
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

  private final AuthProperties authProperties;

  private final AntPathMatcher antPathMatcher = new AntPathMatcher();

  /**
   * @param exchange
   * @param chain
   * @return {@link Mono}<{@link Void}>
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // 1.获取Request
    ServerHttpRequest request = exchange.getRequest();
    // 2.判断是否不需要拦截
    if (isExclude(request.getPath().toString())) {
      // 无需拦截，直接放行
      return chain.filter(exchange);
    }
    String jwt = null;
    // 3.获取请求头中的token
    List<String> headers = request.getHeaders().get("authorization");
    if (headers == null) {
      ServerHttpResponse response = exchange.getResponse();
      response.setRawStatusCode(401);
      return response.setComplete();
    }
    if (!headers.isEmpty()) {
      jwt = headers.get(0);
    }
    //如果请求头中没有token
    if (!StringUtils.hasLength(jwt)) {
      log.info("请求头token为空，返回未登录的信息");
      ServerHttpResponse response = exchange.getResponse();
      response.setRawStatusCode(401);
      return response.setComplete();
    }
    //5解析token，如果解析失败，返回错误结果
    try {
      JwtUtils.parseJwt(jwt);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("解析令牌失败，返回未登录信息");
      ServerHttpResponse response = exchange.getResponse();
      response.setRawStatusCode(401);
      return response.setComplete();
    }
    return chain.filter(exchange);
  }

  /**
   * @param antPath
   * @return boolean
   */
  private boolean isExclude(String antPath) {
    for (String pathPattern : authProperties.getExcludePaths()) {
      if (antPathMatcher.match(pathPattern, antPath)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return int
   */
  @Override
  public int getOrder() {
    return 0;
  }
}
