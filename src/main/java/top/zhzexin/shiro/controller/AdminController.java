package top.zhzexin.shiro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhzexin.shiro.domain.JsonData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin")
@RestController
public class AdminController {

    /**
     * 显示视频的详细信息
     */
    @RequestMapping("showVideoInfo")
    public JsonData showVideoInfo(){
        Map<String,Object> map = new HashMap<>();
        map.put("price","11");
        map.put("name","如何学习");
        map.put("createTime",new Date());
        return JsonData.buildSuccess(map,1);
    }
}
