package top.zhzexin.shiro.config;


import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 通常有三种sessionManager
 * DefaultSessionManager： 默认实现，常用于javase
 * ServletContainerSessionManager: web环境
 * DefaultWebSessionManager：常用于自定义实
 */
public class CustomeSessionManager extends DefaultWebSessionManager {

    //定义请求header的key,一般为token或authorition
    public static final String AUTHORIZATION = "token";

    /**
     * 实现父类构造方法，防止被覆盖
     */
    public CustomeSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //使用webUntil来将ServletRequest等转成HttpServletRequest,并且获取header信息
        String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);

        if (sessionId != null) {
            //以下内容都是查看DefaultWebSessionManager.getSessionId方法处理的，我们只是想自定义获取session的key值
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);

            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            //automatically mark it valid here.  If it is invalid, the
            //onUnknownSession method below will be invoked and we'll remove the attribute at that time.
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);

            return sessionId;

        } else {
            //交给父类去处理sessionid为空的情况
            return super.getSessionId(request, response);
        }

    }
}
