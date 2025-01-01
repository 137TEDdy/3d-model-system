package com.liushenwuzu.pipeline.utils;

import java.util.Random;

/**
 * 随机化字符串的工具.
 */
public class RandomString {

  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";

  /**
   * 获取随机化字符串.
   *
   * @param num 字符串长度
   * @return 字符串
   */
  public static String getRandomString(int num) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < num; i++) {
      int index = random.nextInt(ALPHABET.length());
      char randomChar = ALPHABET.charAt(index);
      sb.append(randomChar);
    }
    String randomString = sb.toString();
    return randomString;
  }
}
