package top.zhzexin.shiro.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import top.zhzexin.shiro.domain.Permission;
import top.zhzexin.shiro.domain.Role;
import top.zhzexin.shiro.domain.User;
import top.zhzexin.shiro.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    /**
     * 用于授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权 doGetAuthorizationInfo");

        String username = (String)principalCollection.getPrimaryPrincipal();
        User user = userService.findAllUserInfoByUsername(username);
        //查询出该用户的角色与权限
        List<String> stringRoleList = new ArrayList<>();
        List<String> stringPermissionList = new ArrayList<>();

        List<Role> roleList = user.getRoleList();
        for(Role role:roleList){
            stringRoleList.add(role.getName());
            List<Permission> permissionList = role.getPermissionList();
            for(Permission permission:permissionList){
                if(permission != null){
                    stringPermissionList.add(permission.getName());
                }
            }
        }


        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(stringPermissionList);
        simpleAuthorizationInfo.addRoles(stringRoleList);
        return simpleAuthorizationInfo;
    }

    /**
     * 用于登录验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("认证 doGetAuthenticationInfo");

        //获取用户输入的账号
        String username = (String) authenticationToken.getPrincipal();
        //查询是否存在
        User allUserInfoByUsername = userService.findAllUserInfoByUsername(username);

        if(allUserInfoByUsername == null || allUserInfoByUsername.getPassword() == "" || allUserInfoByUsername.getPassword() == null){
            return null;
        }

        return new SimpleAuthenticationInfo(username,allUserInfoByUsername.getPassword(),this.getClass().getName());
    }
}
