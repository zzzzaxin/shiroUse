package top.zhzexin.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Reaml,需要继承AuthorizingRealm
 */
public class CustomerReaml extends AuthorizingRealm {

    private final  Map<String,String> userMap = new HashMap<>();
    private final  Map<String, Set<String>> rolesMap = new HashMap<>();
    {
        Set<String> role1 = new HashSet<>();
        Set<String> role2 = new HashSet<>();
        role1.add("role1");
        role1.add("role2");
        role2.add("root");
        rolesMap.put("zexin",role1);
        rolesMap.put("admin",role2);
    }
    private final  Map<String,Set<String>> permissionsMap = new HashMap<>();
    {
        Set<String> permission1 = new HashSet<>();
        Set<String> permission2 = new HashSet<>();
        permission1.add("video:find");
        permission1.add("video:buy");
        permission2.add("video:add");
        permission2.add("video:delete");
        permissionsMap.put("zexin",permission1);
        permissionsMap.put("admin",permission2);
    }

    /**
     * 用于授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("调用了doGetAuthorizationInfo");

        String primaryPrincipal = (String)principalCollection.getPrimaryPrincipal();

        Set<String> permissions = getPermissions(primaryPrincipal);

        Set<String> rolesions = getRolesions(primaryPrincipal);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(rolesions);
        simpleAuthorizationInfo.setStringPermissions(permissions);


        return simpleAuthorizationInfo;
    }

    /**
     * 用于登录
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("调用了doGetAuthenticationInfo");
        String username = (String)authenticationToken.getPrincipal();

        //获取用户密码，这一步实战通常去查数据库
        String password = getPasswordByAccount(username);
        if(password == null || password == ""){
            return null;
        }
        //认证成功后返回
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username,password,this.getName());
        return simpleAuthenticationInfo;
    }

    private String getPasswordByAccount(String name){
        userMap.put("admin","123123");
        userMap.put("zexin","123");
        return userMap.get(name);
    }

    private Set<String> getPermissions(String name){
        return permissionsMap.get(name);
    }

    private Set<String> getRolesions(String name){
        return rolesMap.get(name);
    }
}
