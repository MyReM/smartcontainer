package com.zsf.core.dao;

import com.zsf.core.entity.User;

/**
 * @author yyq
 */
public interface UserDAO extends BaseDAO<User, Long> {

    User findByUserName(String userName);

    User findByUserCode(String userCode);

    User findByUserNameAndPassword(String userName, String password);
}
