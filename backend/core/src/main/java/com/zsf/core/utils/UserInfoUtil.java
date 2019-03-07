package com.zsf.core.utils;

import com.zsf.core.config.shiro.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;

public class UserInfoUtil {

    public static UserInfo getUserInfo() {

        UserInfo userInfo = new UserInfo();
        Subject subject = SecurityUtils.getSubject();
        BeanUtils.copyProperties(subject.getPrincipal(), userInfo);
        return userInfo;
    }
}
