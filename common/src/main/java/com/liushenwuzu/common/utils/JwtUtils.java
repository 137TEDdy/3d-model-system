package com.liushenwuzu.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * jwt工具包
 */
public class JwtUtils {

  private static String SIGNKEY = "itheima";
  private static Long EXPIRE = 43200000L;

  /**
   * 生成JWT令牌
   */
  public static String generateJwt(Map<String, Object> claims) {
    String jwt = Jwts.builder()
        .addClaims(claims)
        .signWith(SignatureAlgorithm.HS256, SIGNKEY)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
        .compact();
    return jwt;
  }

  /**
   * 解析JWT令牌
   *
   * @param jwt JWT令牌
   * @return JWT第二部分负载 payload 中存储的内容
   */
  public static Claims parseJwt(String jwt) {
    Claims claims = Jwts.parser()
        .setSigningKey(SIGNKEY)
        .parseClaimsJws(jwt)
        .getBody();
    return claims;
  }
}
