package com.zsf.core.service;

import com.zsf.core.entity.Role;
import com.zsf.core.entity.User;

import java.util.List;

public interface UserService extends BaseService<User, Long> {

    User findByUserName(String userName);

    User findByUserCode(String userCode);

    User findByUserNameAndPassword(String userName, String password);

    List<Role> findByUserId(Long userId);

}
