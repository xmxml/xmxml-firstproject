package com.itheima.stock.service;

import com.google.common.base.Strings;
import com.itheima.stock.constant.StockConstant;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.pojo.req.LoginReqVo;
import com.itheima.stock.pojo.resp.LoginRespVo;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.pojo.resp.ResponseCode;
import com.itheima.stock.untils.IdWorker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    public SysUser findByName(String userName){
        return sysUserMapper.findByUserName(userName);
    }

    public R<LoginRespVo> login(LoginReqVo reqVo) {
        //验证码不能为空
        if (reqVo==null ||  StringUtils.isBlank(reqVo.getCode()) ||StringUtils.isBlank(reqVo.getRkey())){
            return R.error(ResponseCode.CHECK_CODE_NOT_EMPTY.getMessage());
        }
        String key = (String)redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + reqVo.getRkey());
        //验证码错误
        if (!reqVo.getCode().equals(key)){
            return R.error(ResponseCode.CHECK_CODE_ERROR.getMessage());
        }
        //用户名密码不能为空
        if (Strings.isNullOrEmpty(reqVo.getUsername()) || Strings.isNullOrEmpty(reqVo.getPassword())){
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        String username = reqVo.getUsername();
        //用户名密码错误
        SysUser user = sysUserMapper.findByUserName(username);
        if (user==null || !passwordEncoder.matches(reqVo.getPassword(),user.getPassword())){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }
        LoginRespVo loginRespVo = new LoginRespVo();
        BeanUtils.copyProperties(user,loginRespVo);
        return R.ok(loginRespVo);
    }

    public R<Map> getCaptchaCode() {
        //雪花算数生成一个不唯一的数rkey
        String s = String.valueOf(idWorker.nextId());
        //生成一个验证码
        String s1 = RandomStringUtils.randomNumeric(4);
        //把两个数放进redis中缓存
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX+s,s1,1, TimeUnit.MINUTES);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("code",s1);
        stringStringHashMap.put("rkey",s);
        return R.ok(stringStringHashMap);
    }
}
