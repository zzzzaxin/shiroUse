package top.zhzexin.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhzexin.shiro.domain.JsonData;
import top.zhzexin.shiro.domain.UserQuery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求公共请求，无需认证授权
 */
@RestController
@RequestMapping("/pub")
public class PubController {

    @RequestMapping("/need_login")
    public JsonData need_login(){
        return new JsonData(-2,null,"麻烦请您先登录再进行访问");
    }

    @RequestMapping("/not_permit")
    public JsonData not_permit(){
        return new JsonData(-3,null,"您当前未有权限，如有疑问请联系管理员！");
    }

    @RequestMapping("index")
    public JsonData index(){

        List<String> videoList = new ArrayList<>();
        videoList.add("Mysql零基础入门到实战 数据库教程");
        videoList.add("Redis高并发高可用集群百万级秒杀实战");
        videoList.add("Zookeeper+Dubbo视频教程 微服务教程分布式教程");
        videoList.add("2019年新版本RocketMQ4.X教程消息队列教程");
        videoList.add("微服务SpringCloud+Docker入门到高级实战");

        return JsonData.buildSuccess(videoList);

    }

    @PostMapping("login")
    public JsonData login(UserQuery userQuery, HttpServletRequest request, HttpServletResponse response){
        if(userQuery.getName() == null && userQuery.getPwd() == null){
            return new JsonData(-1,null,"请输入账号或者密码！");
        }
        try {
            Map<String,Object> info = new HashMap<>();
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userQuery.getName(),userQuery.getPwd());
            subject.login(usernamePasswordToken);
            info.put("msg","登录成功");
            info.put("session_id", subject.getSession().getId());
            return JsonData.buildSuccess(info);
        }catch (Exception e){
            return JsonData.buildError("账号或者密码错误");
        }

    }


}
