package top.zhzexin.shiro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhzexin.shiro.domain.JsonData;

@RequestMapping("/authc")
@RestController
public class AuthcController {

    /**
     * 查看个人信息
     */
    @RequestMapping("myInfo")
    public JsonData myInfo(){
        return JsonData.buildSuccess("我叫zzx",1);
    }
}
