package com.zhixue.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.zhixue.demo.constant.ShiroConsts;
import com.zhixue.demo.constant.StatusCodeConsts;
import com.zhixue.demo.exception.GlobalException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Objects;

/**
 * @ClassName UserUtils
 * @Description 用户工具类
 * @Author Tori
 * @Date 2020/1/11 10:23
 * @Version 1.0
 **/
public class UserUtils {

    /**
     * 获取当前登录用户ID
     * @return
     * @throws GlobalException
     */
    public static Long getCurrentUserId() throws GlobalException {
        Subject currentUser = SecurityUtils.getSubject();
        JSONObject user = (JSONObject) currentUser.getSession().getAttribute(ShiroConsts.SESSION_USER_INFO);
        Long userId = user.getLong("id");
        if (Objects.isNull(userId)) {
            throw new GlobalException(StatusCodeConsts.ERROR, "无法获取当前用户ID，有可能登陆已过期");
        }
        return userId;
    }

}
