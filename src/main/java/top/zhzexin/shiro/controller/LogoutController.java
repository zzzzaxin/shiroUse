package top.zhzexin.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhzexin.shiro.domain.JsonData;

@RequestMapping
@RestController
public class LogoutController {


    @RequestMapping("/logout")
    public JsonData logout(){
        Subject subject = SecurityUtils.getSubject();
        if(subject != null){
            subject.logout();
            return JsonData.buildSuccess(null,"退出成功");
        }
        return JsonData.buildError("退出失败");
    }

}
