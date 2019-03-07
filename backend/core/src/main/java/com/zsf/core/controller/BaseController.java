package com.zsf.core.controller;

import com.zsf.core.config.shiro.UserInfo;
import com.zsf.core.config.web.Message;
import com.zsf.core.entity.User;
import com.zsf.core.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yyq
 */
@RestController
@Scope(value = "prototype")
@Validated
public class BaseController {

    @Autowired
    private UserService userService;

    private UserInfo userInfo = new UserInfo();

    public final String SUCCESS = "success";
    public final String FAILED = "failed";

    public UserInfo getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        BeanUtils.copyProperties(subject.getPrincipal(), userInfo);
        return userInfo;
    }

    public User getUser(Long userId) {
        User user = userService.findOne(userId);

        return user;
    }
}
