package com.zhixue.demo.config;

import com.zhixue.demo.entity.UserDO;
import com.zhixue.demo.repository.UserRepository;
import com.zhixue.demo.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName DataInitializer
 * @Description 初始化一个超级管理员账号
 * @Author 孔杰平
 * @Date 2020/1/9 9:25
 * @Version 1.0
 **/
@Component
public class AdminInitializer implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;

    @Value("${initial-data.account}")
    private String adminAccount;

    @Value("${initial-data.password}")
    private String adminPassword;

    public void initUser(String[] info) {
        if (StringUtils.isEmpty(info[0]) || StringUtils.isEmpty(info[1])) {
            return;
        }
        if (userRepository.existsByUsername(info[0])) {
            return;
        }
        UserDO user = new UserDO();
        user.setUsername(info[0]);
        user.setPassword(MD5Utils.md5(info[1]));
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        userRepository.save(user);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (StringUtils.isEmpty(adminAccount) || StringUtils.isEmpty(adminPassword)) {
            return;
        }
        if (userRepository.existsByUsername(adminAccount)) {
            return;
        }

        // 创建管理员账号
        initUser(new String[]{adminAccount, adminPassword});
    }
}
