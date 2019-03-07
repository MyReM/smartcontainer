package com.zsf.core.service;

import com.zsf.core.entity.Menu;
import com.zsf.core.entity.Role;
import com.zsf.core.entity.RoleMenu;

import java.util.List;

/**
 * @author yyq
 */
public interface RoleService extends BaseService<Role, Long> {

    Long findByRoleIdAndMenuId(Long roleId, Long menuId);

    List<Menu> findParentMenu(Long roleId);
}
