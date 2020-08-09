package top.zhzexin.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

/**
 * Realm与SecurityManager结合使用，Realm用于数据查询，存储之类
 * Realm常见的话有三种，一种将iniRealm，JdbcRealm，自定义Realm
 *
 */
public class otherOptionsRealm {






    /**
     * 身份认证，模拟用户登陆
     */
    @Test
    public void quickStart(){



        //使用工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiroConfig.ini");

        SecurityManager securityManager = factory.getInstance();

        //选择securityManager环境
        SecurityUtils.setSecurityManager(securityManager);

        //获取subject实体，实体代表者用户或者第三方之类
        Subject subject = SecurityUtils.getSubject();

        //读者登陆认证是否通过,shiro验证失败都是抛错误出来的，做好try  catch处理
        UsernamePasswordToken authenticationToken = new UsernamePasswordToken("zexin","123");
        subject.login(authenticationToken);

        System.out.println("登陆认证结果：" + subject.isAuthenticated());

        System.out.println("获取用户账号：" + subject.getPrincipal());

        System.out.println("验证用户是否有该角色：" +  subject.hasRole("root"));


        System.out.println("验证用户是否有权限：" +  subject.isPermitted("video:buy"));

        subject.logout();

        System.out.println("logout认证结果：" + subject.isAuthenticated());

        System.out.println("logout获取用户账号：" + subject.getPrincipal());



    }


    /**
     * 身份认证，模拟用户登陆
     */
    @Test
    public void quickStart2(){
        //jdbc 有两种，一种可以将数据库配置信息写在ini配置文件里（不推荐，方法已过期），另一种是在类中直接使用数据源来配置
        //使用jdbcRealm的话，数据库的表结构就得跟shiro源码写的一样，他是将用户，角色，权限与角色的对应关系存在了数据库当中

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://120.76.62.13:3606/xdclass_shiro?characterEncoding=UTF- 8&serverTimezone=UTC&useSSL=false");
        ds.setUsername("test");
        ds.setPassword("Xdclasstest");

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setDataSource(ds);

        securityManager.setRealm(jdbcRealm);



        //使用工厂
        //Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiroJdncConfig.ini");

        //SecurityManager securityManager = factory.getInstance();

        //选择securityManager环境
        SecurityUtils.setSecurityManager(securityManager);

        //获取subject实体，实体代表者用户或者第三方之类
        Subject subject = SecurityUtils.getSubject();

        //读者登陆认证是否通过,shiro验证失败都是抛错误出来的，做好try  catch处理
        UsernamePasswordToken authenticationToken = new UsernamePasswordToken("zexin","123");
        subject.login(authenticationToken);

        System.out.println("登陆认证结果：" + subject.isAuthenticated());

        System.out.println("获取用户账号：" + subject.getPrincipal());

        System.out.println("验证用户是否有该角色：" +  subject.hasRole("root"));


        System.out.println("验证用户是否有权限：" +  subject.isPermitted("video:buy"));

        subject.logout();

        System.out.println("logout认证结果：" + subject.isAuthenticated());

        System.out.println("logout获取用户账号：" + subject.getPrincipal());



    }
}
