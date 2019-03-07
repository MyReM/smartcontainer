package com.zsf.core.service.impl;

import com.zsf.core.dao.PermissionDAO;
import com.zsf.core.entity.Permission;
import com.zsf.core.mapper.PermissionMapper;
import com.zsf.core.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Long> implements PermissionService {

    private PermissionDAO permissionDAO;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    public void setPermissionDAO(PermissionDAO permissionDAO) {
        this.permissionDAO = permissionDAO;
        super.setBaseDAO(permissionDAO);
    }
}
