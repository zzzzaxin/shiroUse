package top.zhzexin.shiro.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 自定义sessionid生成规则
 */
public class CustomeSessionIdGenerator implements SessionIdGenerator {
    @Override
    public Serializable generateId(Session session) {
        return "zzxShiro"+ UUID.randomUUID().toString().replace("-","");
    }
}
