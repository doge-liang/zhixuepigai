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

    Boolean existsByTel(String tel);

    UserDO findUserDOByTel(String tel);

    @Query("select count(u.id)>0 from UserDO as u where tel = ?1 and id <> ?2")
    Boolean existsByTelAndNotId(String tel, Long id);

    @Transactional
    @Modifying
    @Query("update UserDO set deleted = 1 where id = ?1")
    void removeById(Long id);

    @Transactional
    @Modifying
    @Query("update UserDO set deleted = 1 where id in ?1")
    void removeByIdList(List<Long> idList);

    UserDO findUserDOByTelAndPassword(String tel, String password);

    @Query("select a.name from UserDO as a where a.id=?1")
    String getNameById(Long id);

    @Query("select a.name from UserDO as a where a.id in ?1 order by a.id")
    List<String> listNameByIdList(List<Long> idList);

    /**
     * 根据部门查找用户 TODO 部门和用户的级联删除未弄清
     */
    List<UserDO> findAllByDepartmentId(Long departmentId);

    /**
     * 查找具有指定权限的用户
     */
    List<UserDO> findAllByRoleId(Long roleId);
}
