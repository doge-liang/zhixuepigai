package com.zhixue.demo.config.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhixue.demo.constant.ShiroConsts;
import com.zhixue.demo.entity.UserDO;
import com.zhixue.demo.service.UserService;
import com.zhixue.demo.util.HttpKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName UserRealm
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 1:01
 * @Version 1.0
 **/
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        Session session = SecurityUtils.getSubject().getSession();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        Long roleId = (Long) session.getAttribute(ShiroConsts.SESSION_USER_ROLE);
        HashSet<String> roleSet = new HashSet<>();
        roleSet.add("" + roleId);
        authorizationInfo.setRoles(roleSet);

        Set<String> permissionSet = (Set<String>) session.getAttribute(ShiroConsts.SESSION_USER_PERMISSION);
        authorizationInfo.setStringPermissions(permissionSet);

        return authorizationInfo;
    }

    /**
     * 执行 Subject.login() 方法时，会调用此方法
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal(); // TODO
        String password = new String ((char[]) token.getCredentials());

        UserDO user = userService.getUser(username, password);
        if (user == null) {
            throw new UnknownAccountException();
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, getName());  // ???

        Session session = SecurityUtils.getSubject().getSession();

        // 清空用户信息
        session.removeAttribute(ShiroConsts.SESSION_USER_ROLE);
        session.removeAttribute(ShiroConsts.SESSION_USER_PERMISSION);
        session.removeAttribute(ShiroConsts.SESSION_USER_INFO);

        // 将用户信息放入 session 中，待后续使用
/*
        session.setAttribute(ShiroConsts.SESSION_USER_ROLE, user.getRoleId());

        List<PermissionDO> permissionList = rpService.listPermissionsByRoleId(user.getRoleId());
        Set<String> permissionSet = new HashSet<>();
        permissionList.forEach(p -> permissionSet.add(p.getCode()));
        session.setAttribute(ShiroConsts.SESSION_USER_PERMISSION, permissionSet);
*/

        JSONObject userInfo = JSON.parseObject(JSON.toJSONString(user));
        userInfo.remove("password");
        session.setAttribute(ShiroConsts.SESSION_USER_INFO, userInfo);

        return authenticationInfo;
    }

/*
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        String tel = (String) principals.getPrimaryPrincipal();
        return super.isPermitted(principals, permission);
    }

    @Override
    public boolean hasRole(PrincipalCollection principal, String roleIdentifier) {
        String tel = (String) principal.getPrimaryPrincipal();
        return super.hasRole(principal, roleIdentifier);
    }
*/
}
