package com.zsf.core.service.impl;

import com.zsf.core.dao.ResouceDAO;
import com.zsf.core.entity.Resource;
import com.zsf.core.mapper.ResourceMapper;
import com.zsf.core.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class ResouceServiceImpl extends BaseServiceImpl<Resource, Long> implements ResourceService {

    private ResouceDAO resourceDAO;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    public void setResourceDAO(ResouceDAO resourceDAO) {
        this.resourceDAO = resourceDAO;
        super.setBaseDAO(resourceDAO);
    }

}
