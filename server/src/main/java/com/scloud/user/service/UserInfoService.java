package com.scloud.user.service;

import com.scloud.user.entity.UserInfo;

public interface UserInfoService {
    UserInfo findByOpenid(String openid);
}
