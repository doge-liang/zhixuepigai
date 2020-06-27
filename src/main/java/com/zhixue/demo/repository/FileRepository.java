package com.zhixue.demo.repository;

import com.zhixue.demo.entity.FileDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: pms
 * @description: [文件repository]
 * @author: Lee
 * @create: 2019-10-12 16:56
 */
@Repository
public interface FileRepository extends JpaRepository<FileDO, Long>, JpaSpecificationExecutor<FileDO> {

    /**
     * 通过id数组获取文件集合
     * @param idList
     * @return
     */
    @Query("select f from FileDO as f where f.id in ?1 ")
    List<FileDO> findByIdList(List<Long> idList);

    /**
     * 通过id和创建时间戳获取文件
     * @param id
     * @param createTimestamp
     * @return
     */
    @Query("select f from FileDO as f where f.id = ?1 and f.createTime = ?2")
    FileDO getByIdANdCreateTime(Long id, Long createTimestamp);

    @Query("select a.showName from FileDO as a where a.id=?1")
    String getNameById(Long id);

    @Query("select a.showName from FileDO as a where a.id in ?1 order by a.id")
    List<String> listNameByIdList(List<Long> idList);
}
