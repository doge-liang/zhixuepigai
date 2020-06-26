package com.zhixue.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName BaseDO
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 1:05
 * @Version 1.0
 **/
@Data
@MappedSuperclass
public abstract class  BaseDO implements Serializable {

    private static final long serialVersionUID = -8279577993978779788L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_time", nullable = false, columnDefinition = "bigint(15) DEFAULT 0 comment '创建时间'")
    private Long createTime;

    @Column(name = "update_time", nullable = false, columnDefinition = "bigint(15) DEFAULT 0 comment '更新时间'")
    private Long updateTime;

}
