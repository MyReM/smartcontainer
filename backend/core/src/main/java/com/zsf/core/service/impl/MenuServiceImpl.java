package com.zsf.core.service.impl;

import com.zsf.core.dao.MenuDAO;
import com.zsf.core.entity.Menu;
import com.zsf.core.mapper.MenuMapper;
import com.zsf.core.mapper.RoleMenuMapper;
import com.zsf.core.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {

    private MenuDAO menuDAO;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
        super.setBaseDAO(menuDAO);
    }
}
