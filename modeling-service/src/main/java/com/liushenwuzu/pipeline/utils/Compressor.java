package com.liushenwuzu.pipeline.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 压缩工具.
 */
public class Compressor {

  /**
   * 对外方法，打包list中的文件，必须是文件不能是文件夹.
   *
   * @param names 需打包文件
   * @return 压缩包存储路径，跟list中的文件位于同一个目录
   */
  public static String compressFiles(List<String> names) throws IOException {
    if (names == null || names.size() == 0) {
      return null;
    }
    File father = new File(names.get(0)).getParentFile();
    File targetDirectory = new File(father, "target");
    targetDirectory.mkdir();
    for (String name : names) {
      File origin = new File(name);
      if (!origin.renameTo(new File(targetDirectory, origin.getName()))) {
        throw new IOException("文件异常");
      }
    }
    File finalTar = new File(father, "target.tar");
    try {
      compress(targetDirectory, finalTar);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("文件打开失败");
    } catch (ArchiveException e) {
      e.printStackTrace();
    }
    deleteFolder(targetDirectory);
    return finalTar.getAbsolutePath();
  }

  /**
   * 对内，递归删文件夹.
   *
   * @param folder 文件夹
   */
  private static void deleteFolder(File folder) {
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          deleteFolder(file);
        } else {
          file.delete();
        }
      }
    }
    folder.delete();
  }

  private static final int BUFFER_SIZE = 2048; //缓存大小2KB

  /**
   * 对内支持方法，压缩文件夹或文件，文件夹的话会按目录结构打包所有文件.
   *
   * @param sourceFile 源文件或源目录
   * @param targetFile 生成的目标压缩包
   * @throws IOException      文件打开异常
   * @throws ArchiveException 归档异常，类型不对
   */
  private static void compress(File sourceFile, File targetFile)
      throws IOException, ArchiveException {
    // 如果目标文件不存在，创建一个
    if (!targetFile.exists()) {
      targetFile.createNewFile();
    }

    OutputStream outputStream = new FileOutputStream(targetFile);
    ArchiveOutputStream archiveOutputStream = null;
    try {
      // 创建 RAR 归档输出流
      archiveOutputStream = new ArchiveStreamFactory()
          .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
      // 打包文件
      compressRecursive(sourceFile, "", archiveOutputStream);
    } finally {
      // 关闭归档输出流
      if (archiveOutputStream != null) {
        archiveOutputStream.finish();
      }
      // 关闭输出流
      if (outputStream != null) {
        outputStream.close();
      }
    }
  }

  /**
   * 对内支持函数，递归实现压缩.
   *
   * @param file                源文件
   * @param father              压缩包内的父亲路径
   * @param archiveOutputStream 归档类型异常
   * @throws IOException 文件打开异常
   */
  private static void compressRecursive(File file, String father,
                                        ArchiveOutputStream archiveOutputStream)
      throws IOException {
    String fileName = file.getName();
    String fileFullName = new File(father, fileName).getAbsolutePath();
    //System.out.println(fileFullName);

    // 如果是目录，递归打包
    if (file.isDirectory()) {
      ArchiveEntry entry = archiveOutputStream.createArchiveEntry(file, fileFullName);
      archiveOutputStream.putArchiveEntry(entry);
      archiveOutputStream.closeArchiveEntry();

      File[] files = file.listFiles();
      for (File subFile : files) {
        compressRecursive(subFile, fileFullName, archiveOutputStream);
      }
    } else {
      // 如果是文件，直接打包
      InputStream inputStream = new FileInputStream(file);
      ArchiveEntry entry = archiveOutputStream.createArchiveEntry(file, fileFullName);
      archiveOutputStream.putArchiveEntry(entry);

      byte[] buffer = new byte[BUFFER_SIZE];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        archiveOutputStream.write(buffer, 0, length);
      }

      archiveOutputStream.closeArchiveEntry();
      inputStream.close();
    }
  }
}

