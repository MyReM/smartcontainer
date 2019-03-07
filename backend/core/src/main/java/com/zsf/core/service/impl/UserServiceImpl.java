package com.zsf.core.service.impl;

import com.zsf.core.dao.RoleDAO;
import com.zsf.core.dao.UserDAO;
import com.zsf.core.entity.Role;
import com.zsf.core.entity.User;
import com.zsf.core.mapper.UserMapper;
import com.zsf.core.mapper.UserRoleMapper;
import com.zsf.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
        super.setBaseDAO(userDAO);
    }

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public User findByUserName(String userName) {
        return userDAO.findByUserName(userName);
    }

    @Override
    public User findByUserCode(String userCode) {
        return userDAO.findByUserCode(userCode);
    }

    @Override
    public User findByUserNameAndPassword(String userName, String password){
        return userDAO.findByUserNameAndPassword(userName, password);
    }

    @Override
    public List<Role> findByUserId(Long userId){
        List<Long> roleIdList = userRoleMapper.findRoleIdByUserId(userId);

        return roleDAO.findAllById(roleIdList);
    }
}
