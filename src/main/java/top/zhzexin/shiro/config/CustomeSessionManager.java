package top.zhzexin.shiro.config;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * 通常有三种sessionManager
 * DefaultSessionManager： 默认实现，常用于javase
 * ServletContainerSessionManager: web环境
 * DefaultWebSessionManager：常用于自定义实
 */
public class CustomeSessionManager extends DefaultWebSecurityManager {
}
