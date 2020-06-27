package com.zhixue.demo.service;

import com.zhixue.demo.entity.FileDO;
import com.zhixue.demo.exception.GlobalException;

import java.util.List;

/**
 * @program: pms
 * @description: [文件service]
 * @author: Lee
 * @create: 2019-10-12 16:59
 */
public interface FileService{

    /**
     * 通过文件编号数组的序列化字符串获取文件列表
     * @param idListString
     * @return
     */
    List<FileDO> listByIdListString(String idListString);

    FileDO insertOne(FileDO one) throws GlobalException;

    List<FileDO> insertList(List<FileDO> list) throws GlobalException;

    FileDO get(Long id) throws GlobalException;

    /**
     * 通过id和时间戳进行文件下载，添加时间戳，防止乱下载
     * @param id
     * @param timestamp
     * @return
     */
    FileDO getByIdAndUploadTime(Long id, Long timestamp);

}
