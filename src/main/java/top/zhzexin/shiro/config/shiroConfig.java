package top.zhzexin.shiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置的路线图与思路
 * shiroFilterFactoryBean-》 SecurityManager-》 CustomSessionManager CustomRealm-》hashedCredentialsMatcher
 * SessionManager
 * DefaultSessionManager： 默认实现，常用于javase
 * ServletContainerSessionManager: web环境
 * DefaultWebSessionManager：常用于自定义实现
 */
@Configuration
public class shiroConfig {

    /**
     * 配置ShiroFilterFactoryBean
     * 可以参考DefaultFilter类
     * anon：无需认证
     * authc：需认证
     * logout：退出
     * perms：需要哪些权限
     * roles：需要哪些角色
     * user：需要哪些用户
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        System.out.println("执行了shiroConfig.shiroFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //必须设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //开始设置一些访问路径

        //需要登录的路径，如不是前后端分离的话，直接返回登录页面，如login.jsp
        shiroFilterFactoryBean.setLoginUrl("/pub/need_login");

        //登录成功，跳转到首页，如前后端分离，则无需配置
        shiroFilterFactoryBean.setSuccessUrl("/");

        //未授权，跳转路径，通常是跳到403页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/pub/not_permit");

        //配置拦截器路径，这里定义一个map，但是有个坑，就是map需要用LinkedHashMap来实现，因为他需要shiro走拦截器是有顺序的，
        // 如果hashMap实现，无序，会造成拦截时有时无的现象出现
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();

        //退出过滤器
        filterChainDefinitionMap.put("logout","logout");

        //匿名访问,也就是游客访问
        filterChainDefinitionMap.put("/pub/**","anon");

        //登录访问
        filterChainDefinitionMap.put("/authc/**","authc");

        //管理员角色访问
        filterChainDefinitionMap.put("/admin/**","roles[admin]");

        //有编辑权限才可以访问
        filterChainDefinitionMap.put("/api/update","perms[video:update]");
        filterChainDefinitionMap.put("/api/buy","perms[video:buy]");

        //坑二: 过滤链是顺序执行，从上而下，一般讲/** 放到最下面,通常其他路径未匹配到的 默认需登录才可访问
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 配置SecurityManager
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(sessionManager());
        //网友透露，sessionManager先设置会好点
        defaultWebSecurityManager.setRealm(customRealm());
        //设置自定义的cacheManager
        defaultWebSecurityManager.setCacheManager(cacheManager());
        return defaultWebSecurityManager;
    }

    /**
     * 配置自定义realm
     * 并且设置上我们加密算法
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

    /**
     * 配置hashedCredentialsMatcher
     * 通常是对我们密码加密算法进行设置
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置我们数据对密码的加密算法，散列算法md5
        credentialsMatcher.setHashAlgorithmName("md5");
        //通常我们可能不止加密一次，当设2时，等于md5(md5(xxx))
        credentialsMatcher.setHashIterations(1);
        return credentialsMatcher;
    }

    /**
     * 配置SessionManager
     * 自定义sessionManager
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        CustomeSessionManager customeSessionManager = new CustomeSessionManager();
        //设置未操作时，session超时时间，单位是毫秒
        customeSessionManager.setGlobalSessionTimeout(20000);
        customeSessionManager.setSessionDAO(sessionDAO());
        return customeSessionManager;
    }

    /**
     * 配置redisManager
     */
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("localhost");
        redisManager.setPort(6379);
        return  redisManager;
    }

    /**
     * 配置CacheManager
     */
    public CacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //设置缓存过期时间，防止操作员在后台设置权限时，缓存权限一直没更新，单位s
        redisCacheManager.setExpire(20);
        return redisCacheManager;
    }

    /**
     * 配置redis的SessionDao
     * 解决session持久化问题
     */
    public SessionDAO sessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(new CustomeSessionIdGenerator());
        return redisSessionDAO;
    }

}
