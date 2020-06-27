package com.zhixue.demo.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: pms
 * @description: [文件实体]
 * @author: Lee
 * @create: 2019-10-12 16:55
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_file")
@ApiModel(description = "文件实体类")
public class FileDO extends BaseDO{

    private static final long serialVersionUID = -2436206456680100055L;

    @Column(name = "show_name", nullable = false, columnDefinition = "varchar(250) comment '文件显示名称（含后缀）'")
    private String showName;

    @Column(name = "storage_name", nullable = false, columnDefinition = "varchar(250) comment '文件存储名称（含后缀）'")
    private String storageName;

    @Column(nullable = false, columnDefinition = "varchar(150) comment '文件路径'")
    private String url;
}
