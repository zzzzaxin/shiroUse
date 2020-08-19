package top.zhzexin.shiro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhzexin.shiro.domain.JsonData;

@RequestMapping("/api")
@RestController
public class ApiController {


    @RequestMapping("/update")
    public JsonData update(){
        return JsonData.buildSuccess(null,"修改成功");
    }

    @RequestMapping("/buy")
    public JsonData buy(){
        return JsonData.buildSuccess(null,"购买成功");
    }
}
