package com.zhixue.demo.repository;

import com.zhixue.demo.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName UserRepository
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 0:56
 * @Version 1.0
 **/
@Repository
public interface UserRepository extends JpaRepository<UserDO, Long>, JpaSpecificationExecutor<UserDO> {

    Boolean existsByUsername(String username);

    UserDO findUserDOByUsername(String username);

    @Query("select count(u.id)>0 from UserDO as u where tel = ?1 and id <> ?2")
    Boolean existsByTelAndNotId(String tel, Long id);

    UserDO findUserDOByUsernameAndPassword(String username, String password);

    @Query("select a.username from UserDO as a where a.id=?1")
    String getNameById(Long id);

    @Query("select a.username from UserDO as a where a.id in ?1 order by a.id")
    List<String> listNameByIdList(List<Long> idList);

    void removeById(Long id);

}
