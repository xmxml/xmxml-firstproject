package com.itheima.stock.controller;


import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.pojo.req.LoginReqVo;
import com.itheima.stock.pojo.resp.LoginRespVo;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(value = "用户认证相关接口定义",tags = "用户功能-用户登录功能")
public class usercontroller {

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    @ApiOperation(value = "根据用户名查询用户信息",notes = "用户信息查询",response = SysUser.class)
    public SysUser findByName(@PathVariable("userName") String userName){
        return userService.findByName(userName);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录功能",notes = "用户登录",response = R.class)
    public R<LoginRespVo> login(@RequestBody LoginReqVo reqVo){
        return userService.login(reqVo);
    }

    @GetMapping("/captcha")
    @ApiOperation(value = "验证码生成功能",response = R.class)
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }



}
