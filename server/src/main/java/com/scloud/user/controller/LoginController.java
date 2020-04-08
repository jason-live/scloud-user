package com.scloud.user.controller;

import com.scloud.user.constant.CookieConstant;
import com.scloud.user.constant.RedisConstant;
import com.scloud.user.entity.UserInfo;
import com.scloud.user.enums.ResultEnum;
import com.scloud.user.enums.RoleEnum;
import com.scloud.user.service.UserInfoService;
import com.scloud.user.utils.CookieUtil;
import com.scloud.user.utils.ResultVoUtil;
import com.scloud.user.vo.ResultVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/buyer")
    public ResultVo buyer(@RequestParam("openid") String openid, HttpServletResponse response) {
        // openid和数据库匹配
        UserInfo userInfo = userInfoService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVoUtil.error(ResultEnum.LOGIN_ERROR);
        }
        // 判断角色
        if (RoleEnum.BUYER.getCode() != userInfo.getRole()) {
            return ResultVoUtil.error(ResultEnum.ROLE_ERROR);
        }
        // cookie设置abc
        CookieUtil.set(response, CookieConstant.OPENID, openid, CookieConstant.expire);

        return ResultVoUtil.success();
    }

    @GetMapping("/seller")
    public ResultVo seller(@RequestParam("openid") String openid, HttpServletRequest request, HttpServletResponse response) {
        // 判断是否已经登录
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null && !StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, cookie.getValue())))) {
            return ResultVoUtil.success();
        }

        // openid和数据库匹配
        UserInfo userInfo = userInfoService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVoUtil.error(ResultEnum.LOGIN_ERROR);
        }
        // 判断角色
        if (RoleEnum.SELLER.getCode() != userInfo.getRole()) {
            return ResultVoUtil.error(ResultEnum.ROLE_ERROR);
        }

        // redis设置key=UUID value=xyz
        String token = UUID.randomUUID().toString();
        Integer expire = CookieConstant.expire;
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE, token), openid, expire, TimeUnit.SECONDS);

        // cookie设置xyz
        CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.expire);
        return  ResultVoUtil.success();
    }
}
