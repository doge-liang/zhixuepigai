package com.zhixue.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName UserDO
 * @Description 用户实体类
 * @Author Tori
 * @Date 2020/6/1 1:50
 * @Version 1.0
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_user")
public class UserDO extends BaseDO {

    private String username;

    private String password;

}
