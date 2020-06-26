package com.zhixue.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhixue.demo.constant.ShiroConsts;
import com.zhixue.demo.constant.StatusCodeConsts;
import com.zhixue.demo.entity.UserDO;
import com.zhixue.demo.exception.GlobalException;
import com.zhixue.demo.repository.UserRepository;
import com.zhixue.demo.service.UserService;
import com.zhixue.demo.util.MD5Utils;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
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

    @Autowired
    private RoleRepository roleRepository;

    @Value("${user.default-password}")
    private String DEFAULT_PASSWORD;

    @Override
    public UserDO getUser(String tel, String password) {
        return repository.findUserDOByTelAndPassword(tel, password);
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
        if (repository.existsByTel(one.getTel())) {
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "手机号" + one.getTel() + "已存在");
        }
        if (Objects.nonNull(one.getRoleId()) && !positionRepository.existsById(one.getRoleId()))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "指定的职务不存在");
        one.setCreateTime(System.currentTimeMillis());
        one.setUpdateTime(System.currentTimeMillis());
        one.setPassword(MD5Utils.md5(DEFAULT_PASSWORD));
        one.setId(null);
//        one.setStatus(UserStatusConsts.PENDING);    // 讲用户设置为待审核状态
        return repository.save(one);
    }

    @Override
    public List<UserDO> insertList(List<UserDO> list) throws GlobalException {
        for (UserDO user: list) {
            insertOne(user);
        }
        return list;
    }

    @Override
    public UserDO get(Long id) throws GlobalException {
        if (Objects.nonNull(id) && id > 0) {
            return repository.findById(id).orElse(null);
        }
        return null;
    }

    @Override
    public Page<UserDO> list(QueryConditionDTO queryCondition) throws GlobalException {
        return null;
    }

    @Override
    public Page<UserVO> listVO(QueryConditionDTO queryCondition) throws GlobalException {
        // 分页与排序
        Pageable pageable = PageRequest.of(queryCondition.getPageIndex() - 1, queryCondition.getPageSize(), Sort.Direction.DESC, "createTime");

        JSONObject condition = queryCondition.getCondition();

        Page<UserDO> doPage = repository.findAll((Specification<UserDO>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // 关键词搜索
            String keyword = condition.getString("keyword");
            if (Objects.nonNull(keyword)) {
                List<Predicate> searchPreList = new ArrayList<>();
                searchPreList.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + keyword.trim() + "%"));
//                    searchPreList.add(criteriaBuilder.like(root.get("tel").as(String.class), "%" + keyword.trim() + "%"));

                predicateList.add(criteriaBuilder.or(searchPreList.toArray(new Predicate[searchPreList.size()])));
            }

            // 类型过滤
            List<Predicate> typePreList = new ArrayList<>();

            List typeList = condition.getJSONArray("type");
            if (Objects.nonNull(typeList)) {
                // 在职用户
                if (typeList.contains("enabled"))
                    typePreList.add(criteriaBuilder.equal(root.get("status").as(Integer.class), UserStatusConsts.ENABLED));
                // 离职用户
                if (typeList.contains("disabled"))
                    typePreList.add(criteriaBuilder.equal(root.get("status").as(Integer.class), UserStatusConsts.DISABLED));
                // 待审核用户
                if (typeList.contains("pending"))
                    typePreList.add(criteriaBuilder.equal(root.get("status").as(Integer.class), UserStatusConsts.PENDING));
                // 待分配用户
                if (typeList.contains("unassigned"))
                    typePreList.add(criteriaBuilder.equal(root.get("status").as(Integer.class), UserStatusConsts.UNASSIGNED));
            }
            if (typePreList.size() > 0)
                predicateList.add(criteriaBuilder.or(typePreList.toArray(new Predicate[typePreList.size()])));

            if (condition.containsKey("roleId") && Objects.nonNull(condition.getLong("roleId"))) {
                predicateList.add(criteriaBuilder.equal(root.get("roleId").as(Long.class), condition.getLong("roleId")));
            }

            predicateList.add(criteriaBuilder.equal(root.get("deleted").as(Boolean.class), false));

            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);

        Page<UserVO> voPage = doPage.map(userDO -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userDO, userVO);
            Long positionId = userDO.getPositionId();
            Long departmentId = userDO.getDepartmentId();
            Long roleId = userDO.getRoleId();
            if (Objects.nonNull(positionId))
                userVO.setPositionName(positionRepository.findById(userDO.getPositionId()).orElse(new PositionDO()).getPositionName());
            if (Objects.nonNull(departmentId))
                userVO.setDepartmentName(departmentRepository.findById(userDO.getDepartmentId()).orElse(new DepartmentDO()).getName());
            if (Objects.nonNull(roleId))
                userVO.setRoleName(roleRepository.findById(userDO.getRoleId()).orElse(new RoleDO()).getName());
            return userVO;
        });

        return voPage;

    }

    @Override
    public Collection<UserLiteDTO> listIdName() {
        return repository.listIdAndName();
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
        // 检查要修改的手机号是否被其他用户占用
        if (repository.existsByTelAndNotId(one.getTel(), one.getId()))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "手机号" + one.getTel() + "已存在");
        Long id = one.getId();
        if (Objects.isNull(id))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "非法操作(编号为空)");
        if (!repository.existsById(id))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "指定的用户不存在");
        oldOne = repository.findById(id).orElse(null);
        if (Objects.isNull(oldOne))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "用户不存在或已删除");
        // 启用前检查是否已分配职务
/*
        if (Objects.equals(one.getStatus(), UserStatusConsts.ENABLED) && Objects.isNull(one.getRoleId()))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "未分配职务，无法启用用户");
*/
        if (Objects.nonNull(one.getRoleId()) && !positionRepository.existsById(one.getRoleId()))
            throw new GlobalException(StatusCodeConsts.BAD_REQUEST, "指定的职务不存在");

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

    @Override
    public void removeList(List<Long> idList) throws GlobalException {
        if (Objects.nonNull(idList) && !idList.isEmpty()) {
            repository.removeByIdList(idList);
        }
    }
}