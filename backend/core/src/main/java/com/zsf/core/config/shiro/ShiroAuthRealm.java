package com.zsf.core.config.shiro;

import com.zsf.core.entity.Log;
import com.zsf.core.entity.Role;
import com.zsf.core.entity.User;
import com.zsf.core.service.RoleService;
import com.zsf.core.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yyq
 */
public class ShiroAuthRealm extends AuthorizingRealm {

    private Log log = new Log();

    private UserService userService;

    private RoleService roleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 授权
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User uesr = (User) principals.getPrimaryPrincipal();

        List<Role> roles = userService.findByUserId(uesr.getUserId());

        List<String> roleString = roles.stream().map(Role::getRoleName).collect(Collectors.toList());

        authorizationInfo.addRoles(roleString);

//        for (SysRole role : userInfo.getRoleList()) {
//            authorizationInfo.addRole(role.getRole());
//            for (SysPermission p : role.getPermissions()) {
//                authorizationInfo.addStringPermission(p.getPermission());
//            }
//        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //获取用户的输入的账号.
        String userCode = (String) token.getPrincipal();

        String password = new String((char[]) token.getCredentials());

//        System.out.println(token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法

        User user = userService.findByUserCode(userCode);

        if (Objects.isNull(user)) {
            throw new UnknownAccountException("用户不存在！");
        }

        if (!Objects.equals(user.getStatus(), User.Status.NORMAL)) { //账户冻结
            throw new LockedAccountException();
        }

        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());

        UserInfo userInfo = UserInfo.builder().userCode(userCode).userName(user.getUserName()).password(password).userId(user.getUserId()).build();

        return this.auth(userInfo, user.getPassword(), credentialsSalt, getName());

    }

    public SimpleAuthenticationInfo auth(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName){

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, realmName);

        return authenticationInfo;
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {

        super.clearCachedAuthenticationInfo(principals);
    }
}
