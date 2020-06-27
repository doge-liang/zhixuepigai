package com.zhixue.demo.service;

import com.zhixue.demo.entity.UserDO;
import com.zhixue.demo.exception.GlobalException;
import lombok.Synchronized;

import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 0:58
 * @Version 1.0
 **/
public interface UserService {

    // TODO 登陆相关

    /**
     * 使用手机号和密码获取用户
     * @param tel
     * @param password
     * @return
     */
    UserDO getUser(String tel, String password);

    /**
     * 用户登录
     * @param tel
     * @param password
     * @return
     */
    Boolean authLogin(String tel, String password);

    /**
     * 退出登录
     *
     * @throws GlobalException
     */
    void loginOut() throws GlobalException;

    UserDO insertOne(UserDO one) throws GlobalException;

    UserDO get(Long id) throws GlobalException;

    /**
     * 根据ID列表获取姓名列表
     * @param idList
     * @return
     * @throws GlobalException
     */
    List<String> listNameByIds(List<Long> idList) throws GlobalException;

    UserDO update(UserDO one, String[] ignoreFields) throws GlobalException;

    /**
     * 更新自己的密码
     *
     * @param password
     * @throws GlobalException
     */
    void updatePassword(String password) throws GlobalException;

    void removeOne(Long id) throws GlobalException;
}
