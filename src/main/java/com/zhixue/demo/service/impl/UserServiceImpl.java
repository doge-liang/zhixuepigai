package com.zhixue.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhixue.demo.constant.ShiroConsts;
import com.zhixue.demo.constant.StatusCodeConsts;
import com.zhixue.demo.entity.UserDO;
import com.zhixue.demo.exception.GlobalException;
import com.zhixue.demo.repository.UserRepository;
import com.zhixue.demo.service.UserService;
import com.zhixue.demo.util.MD5Utils;
import com.zhixue.demo.util.UserUtils;
import lombok.Synchronized;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 0:58
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDO getUser(String tel, String password) {
        return repository.findUserDOByUsernameAndPassword(tel, password);
    }

    @Override
    public Boolean authLogin(String tel, String password) {
        Subject currentUser = SecurityUtils.getSubject();
        String md5Password = MD5Utils.md5(password);
        UsernamePasswordToken token = new UsernamePasswordToken(tel, md5Password);

        try {
            currentUser.login(token);
        } catch (IncorrectCredentialsException ex) {
            ex.printStackTrace();
            return false;
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void loginOut() throws GlobalException {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            JSONObject user = (JSONObject) currentUser.getSession().getAttribute(ShiroConsts.SESSION_USER_INFO);
            currentUser.logout();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(500, "退出失败");
        }
    }

    @Synchronized
    @Override
    public UserDO insertOne(UserDO one) throws GlobalException {
        if (repository.existsByUsername(one.getUsername())) {
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "用户名" + one.getUsername() + "已存在");
        }
/*
        if (Objects.nonNull(one.getRoleId()) && !positionRepository.existsById(one.getRoleId()))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "指定的职务不存在");
*/
        one.setCreateTime(System.currentTimeMillis());
        one.setUpdateTime(System.currentTimeMillis());
        one.setPassword(MD5Utils.md5(one.getPassword()));
        one.setId(null);
        return repository.save(one);
    }

    @Override
    public UserDO get(Long id) throws GlobalException {
        if (Objects.nonNull(id) && id > 0) {
            return repository.findById(id).orElse(null);
        }
        return null;
    }


    @Override
    public List<String> listNameByIds(List<Long> idList) throws GlobalException {
        List<String> list = new ArrayList<>();
        for (Long id : idList) {
            list.add(repository.getNameById(id));
        }
        return list;
    }

    @Synchronized
    @Override
    public UserDO update(UserDO one, String[] ignoreFields) throws GlobalException {
        UserDO oldOne = null;
        if (Objects.isNull(one))
            return null;
        // 检查要修改的用户名是否被其他用户占用
        if (repository.existsByTelAndNotId(one.getUsername(), one.getId()))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "手机号" + one.getUsername() + "已存在");
        Long id = one.getId();
        if (Objects.isNull(id))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "非法操作(编号为空)");
        if (!repository.existsById(id))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "指定的用户不存在");
        oldOne = repository.findById(id).orElse(null);
        if (Objects.isNull(oldOne))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "用户不存在或已删除");

        BeanUtils.copyProperties(one, oldOne, "id", "createTime", "updateTime", "password");
        oldOne.setUpdateTime(System.currentTimeMillis());
        return repository.save(oldOne);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password) throws GlobalException {
        Long userId = UserUtils.getCurrentUserId();
        if (Strings.isBlank(password)) {
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "密码不能为空");
        }
        UserDO one = get(userId);
        if (one != null) {
            one.setPassword(MD5Utils.md5(password));
            one.setUpdateTime(System.currentTimeMillis());
            repository.save(one);
        }
    }

    @Override
    public void removeOne(Long id) throws GlobalException {
        if (Objects.isNull(id) || id < 0) {
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "参数错误");
        }
        if (!repository.existsById(id))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "请求的用户不存在");
        repository.removeById(id);
    }
}