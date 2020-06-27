package com.zhixue.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.zhixue.demo.entity.FileDO;
import com.zhixue.demo.exception.GlobalException;
import com.zhixue.demo.repository.FileRepository;
import com.zhixue.demo.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: pms
 * @description: [文件service实现类]
 * @author: Lee
 * @create: 2019-10-12 17:00
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository repository;

    @Override
    public FileDO insertOne(FileDO one) throws GlobalException {
        if (Objects.nonNull(one)) {
            repository.save(one);
        }
        return null;
    }

    /**
     * 方法未编写
     */
    @Override
    public List<FileDO> insertList(List<FileDO> list) throws GlobalException {
        return null;
    }

    @Override
    public FileDO get(Long id) throws GlobalException {
        if (Objects.nonNull(id)) {
            return repository.findById(id).orElse(null);
        }
        return null;
    }

    @Override
    public FileDO getByIdAndUploadTime(Long id, Long timestamp) {
        if (Objects.nonNull(id) && Objects.nonNull(timestamp)) {
            return repository.getByIdANdCreateTime(id, timestamp);
        }
        return null;
    }

    @Override
    public List<FileDO> listByIdListString(String idListString) {
        List<FileDO> list = new ArrayList<FileDO>();
        try {
            if (Strings.isNotBlank(idListString)) {
                JSONArray jsonArray = JSONArray.parseArray(idListString);
                Set<Long> idSet = new HashSet<Long>();
                if (null != jsonArray)
                    for (int i = 0; i < jsonArray.size(); i++) {
                        idSet.add(jsonArray.getLong(i));
                    }
                if (0 < idSet.size()) {
                    list = repository.findByIdList(new LinkedList<Long>(idSet));
                }
            }
        }catch(Exception e) {
            // 防止字符串转换失败导致显示异常
            return list;
        }
        return list;
    }
}
