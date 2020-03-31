package com.scloud.user.controller;

import com.scloud.user.constant.CookieConstant;
import com.scloud.user.entity.UserInfo;
import com.scloud.user.enums.ResultEnum;
import com.scloud.user.enums.RoleEnum;
import com.scloud.user.service.UserInfoService;
import com.scloud.user.utils.CookieUtil;
import com.scloud.user.utils.ResultVoUtil;
import com.scloud.user.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

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
    public ResultVo seller(@RequestParam("openid") String openid, HttpServletResponse response) {
        return  ResultVoUtil.success();
    }
}
