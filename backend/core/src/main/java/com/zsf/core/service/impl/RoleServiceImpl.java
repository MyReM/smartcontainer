package com.zsf.core.service.impl;

import com.zsf.core.dao.RoleDAO;
import com.zsf.core.entity.Menu;
import com.zsf.core.entity.Role;
import com.zsf.core.entity.RoleMenu;
import com.zsf.core.mapper.RoleMapper;
import com.zsf.core.mapper.RoleMenuMapper;
import com.zsf.core.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    private RoleDAO roleDAO;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
        super.setBaseDAO(roleDAO);
    }

    @Override
    public Long findByRoleIdAndMenuId(Long roleId, Long menuId){
        return roleMenuMapper.findByRoleIdAndMenuId(roleId, menuId);
    }

    @Override
    public List<Menu> findParentMenu(Long roleId) {
        return roleMenuMapper.findParentMenu(roleId);
    }
}
