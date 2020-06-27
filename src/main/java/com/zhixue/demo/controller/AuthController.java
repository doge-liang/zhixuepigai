package com.zhixue.demo.controller;

import com.zhixue.demo.dto.ResultDTO;
import com.zhixue.demo.entity.UserDO;
import com.zhixue.demo.enumeration.ResultEnum;
import com.zhixue.demo.exception.GlobalException;
import com.zhixue.demo.repository.UserRepository;
import com.zhixue.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AuthController
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 0:59
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    /**
     * 登录
     *
     * @param one
     * @return
     */
    @PostMapping("/login")
    public ResultDTO<?> authLogin(@RequestBody UserDO one) {
        boolean auth = userService.authLogin(one.getUsername(), one.getPassword());
        if (auth) {
            return ResultDTO.of(ResultEnum.SUCCESS, "登陆成功");
        }
        return ResultDTO.of(ResultEnum.BAD_REQUEST, "账号或密码错误");
    }

    /**
     * 注销登录
     *
     * @return
     * @throws GlobalException
     */
    @GetMapping("/logout")
    public ResultDTO<?> logout() throws GlobalException {
        userService.loginOut();
        return ResultDTO.of(ResultEnum.SUCCESS, "注销成功");
    }


}
