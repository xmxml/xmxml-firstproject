package com.itheima.stock.controller;


import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.pojo.req.LoginReqVo;
import com.itheima.stock.pojo.resp.LoginRespVo;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class usercontroller {

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public SysUser findByName(@PathVariable("userName") String userName){
        return userService.findByName(userName);
    }

    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo reqVo){
        return userService.login(reqVo);
    }

    @GetMapping("/captcha")
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }



}
