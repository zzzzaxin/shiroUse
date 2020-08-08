package top.zhzexin.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * 用于测试shiro的快速启动，配置，以及身份认证与权限授权
 * 执行顺序 @BeforeClass -》 @Before ——》 @Test -》@After ——》@AfterClass
 * 通常@BeforeClass/@AfterClass用的很少，只会执行一次，而其他的用的比较多，调用每个用例测试都会执行一次
 */
public class QiuckStartShiro {

    private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    private DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();


    /**
     * 初始化shiro的环境SecurityManger环境，以及配置Raml数据源
     */
    @Before
    public void init(){

        //手动添加数据源
        simpleAccountRealm.addAccount("zexin","123","root","admin");
        simpleAccountRealm.addAccount("zhangzexin","234","user");

        //指定数据源（reaml）
        defaultSecurityManager.setRealm(simpleAccountRealm);

    }


    /**
     * 身份认证，模拟用户登陆
     */
    @Test
    public void quickStartAuthentition(){
        //选择securityManager环境
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //获取subject实体，实体代表者用户或者第三方之类
        Subject subject = SecurityUtils.getSubject();

        //读者登陆认证是否通过,shiro验证失败都是抛错误出来的，做好try  catch处理
        UsernamePasswordToken authenticationToken = new UsernamePasswordToken("zexin","123");
        subject.login(authenticationToken);

        System.out.println("登陆认证结果：" + subject.isAuthenticated());

        System.out.println("获取用户账号：" + subject.getPrincipal());

        System.out.println("验证用户是否有该角色：" +  subject.hasRole("root"));

        subject.logout();

        System.out.println("logout认证结果：" + subject.isAuthenticated());

        System.out.println("logout获取用户账号：" + subject.getPrincipal());



    }
}
