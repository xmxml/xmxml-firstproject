package com.itheima.stock.service;

import com.google.common.base.Strings;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.pojo.req.LoginReqVo;
import com.itheima.stock.pojo.resp.LoginRespVo;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.pojo.resp.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SysUser findByName(String userName){
        return sysUserMapper.findByUserName(userName);
    }

    public R<LoginRespVo> login(LoginReqVo reqVo) {
        if (reqVo==null || Strings.isNullOrEmpty(reqVo.getUsername()) || Strings.isNullOrEmpty(reqVo.getPassword())){
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        String username = reqVo.getUsername();
        SysUser user = sysUserMapper.findByUserName(username);
        if (user==null || !passwordEncoder.matches(reqVo.getPassword(),user.getPassword())){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }
        LoginRespVo loginRespVo = new LoginRespVo();
        BeanUtils.copyProperties(user,loginRespVo);
        return R.ok(loginRespVo);
    }
}
