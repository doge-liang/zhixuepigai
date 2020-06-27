package com.zhixue.demo.controller;

import com.zhixue.demo.dto.ResultDTO;
import com.zhixue.demo.entity.UserDO;
import com.zhixue.demo.enumeration.ResultEnum;
import com.zhixue.demo.exception.GlobalException;
import com.zhixue.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @program: internal_system
 * @description: [权限管理-管理员帐号管理controller]
 * @author: Lee & 孔杰平
 * @create: 2019-09-03 11:18
 */
@RestController
@RequestMapping("/admin/user")
@Validated
@Slf4j
public class UserController{

    @Autowired
    private UserService service;

    /**
     * 插入单个用户
     *
     * @param one
     * @return
     * @throws GlobalException
     */
    @PostMapping
    public ResultDTO<?> insertOne(@RequestBody @Valid UserDO one) throws GlobalException {
        service.insertOne(one);
        return ResultDTO.of(ResultEnum.INSERT_SUCCESS);
    }


    /**
     * 获取单个用户
     *
     * @param userId
     * @return
     * @throws GlobalException
     */
    @GetMapping("/{userId}")
    public ResultDTO<UserDO> get(@PathVariable Long userId) throws GlobalException {
        UserDO user = service.get(userId);
        if (Objects.isNull(user))
            throw new GlobalException(ResultEnum.BAD_REQUEST);
        return ResultDTO.of(ResultEnum.SUCCESS, user);
    }


    /**
     * @类名 : UserController
     * @说明 : 更新用户信息
     * @创建日期 : 2019/9/28
     * @作者 : 孔杰平
     * @版本 : 1.0
     */
    @PutMapping("/{userId}")
    public ResultDTO<?> update(@RequestBody @Valid UserDO one) throws GlobalException {
        service.update(one, new String[]{"id", "createTime", "updateTime", "password"});
        return ResultDTO.of(ResultEnum.UPDATE_SUCCESS);
    }

    /**
     * @param
     * @return
     * @throws GlobalException
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/updatePwd")
    public ResultDTO updatePwd(@RequestBody String password) throws GlobalException {
        service.updatePassword(password);
        return ResultDTO.of(ResultEnum.UPDATE_SUCCESS);
    }


    /**
     * 删除单个用户
     *
     * @param userId
     * @return
     * @throws GlobalException
     */
    @DeleteMapping("/{userId}")
    public ResultDTO<?> removeOne(@PathVariable Long userId) throws GlobalException {
        service.removeOne(userId);
        return ResultDTO.of(ResultEnum.DELETE_SUCCESS);
    }

}
