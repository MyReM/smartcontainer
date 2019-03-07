package com.zsf.core.service.impl;

import com.zsf.core.dao.DeptDAO;
import com.zsf.core.entity.Dept;
import com.zsf.core.mapper.DeptMapper;
import com.zsf.core.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class DeptServiceimpl extends BaseServiceImpl<Dept, Long> implements DeptService {

    private DeptDAO deptDAO;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    public void setDeptDAO(DeptDAO deptDAO) {
        this.deptDAO = deptDAO;
        super.setBaseDAO(deptDAO);
    }

}
