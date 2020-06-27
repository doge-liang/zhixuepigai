package com.zhixue.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @program: pms
 * @description: [文件工具]
 * @author: Lee
 * @create: 2019-10-12 17:12
 */
@Slf4j
public final class FileUtils {

    /**
     * 文件存储
     *
     * @param file     文件字节数组
     * @param filePath 文件存储路径
     * @param fileName 文件名称
     * @throws Exception
     */
    public static void saveFile(byte[] file, String filePath, String fileName) throws IOException {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out;
        out = new FileOutputStream(filePath + "/" + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 删除本地文件
     *
     * @param filePath
     * @param fileName
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath + fileName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 前台判断是否是图片文件
     *
     * @param file
     * @return
     */
    public static boolean isPicture(MultipartFile file) {
        if (null == file) {
            return false;
        }
        // 图片后缀
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
			return false;
		}
        int index = originalFilename.lastIndexOf(".");
        String suString = originalFilename.substring(index);
        if (StringUtils.isEmpty(suString)) {
			return false;
		}
        String suffixName = suString.toLowerCase();
        if (suffixName.indexOf("png") > 0
                || suffixName.indexOf("jpg") > 0
                || suffixName.indexOf("jpeg") > 0) {
            return true;
        }
        return false;
    }

    /**
     * 后台判断是否是合法文件
     *
     * @param file
     * @return
     */
    public static boolean isLegalFile(MultipartFile file) {
        if (null == file) {
            return false;
        }
        // 图片后缀
        String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
        if (suffixName.indexOf("png") > 0
                || suffixName.indexOf("jpg") > 0
                || suffixName.indexOf("jpeg") > 0
                || suffixName.indexOf("doc") > 0
                || suffixName.indexOf("docx") > 0
                || suffixName.indexOf("txt") > 0
                || suffixName.indexOf("xlsx") > 0
                || suffixName.indexOf("xls") > 0
                || suffixName.indexOf("pdf") > 0
                || suffixName.indexOf("ppt") > 0
                || suffixName.indexOf("pptx") > 0
                || suffixName.indexOf("mp4") > 0
                || suffixName.indexOf("avi") > 0
                || suffixName.indexOf("tar") > 0
                || suffixName.indexOf("zip") > 0
                || suffixName.indexOf("rar") > 0
                || suffixName.indexOf("7z") > 0
                || suffixName.indexOf("mkv") > 0) {
            return true;
        }
        return false;
    }

    /**
     * 链接url下载图片
     * @param fileLocalUrl  图片
     * @param fileRemoteUrl
     * @return
     */
    public static Boolean downloadFile(String fileRemoteUrl,String fileLocalUrl) {
        URL url = null;
        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;
        boolean flag = true;
        try{
            url = new URL(fileRemoteUrl);
            dataInputStream = new DataInputStream(url.openStream());

            fileOutputStream = new FileOutputStream(new File(fileLocalUrl));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
        } catch (MalformedURLException e) {
        	log.error("MalformedURLException 异常：{}",e.getMessage());
        	flag = false;
        } catch (IOException e) {
        	log.error("IOException 异常：{}",e.getMessage());
        	flag = false;
        }finally {
        	IOUtils.closeQuietly(fileOutputStream);
        	IOUtils.closeQuietly(dataInputStream);
		}
        return flag;
    }
}
