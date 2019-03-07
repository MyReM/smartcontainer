package com.zsf.container.service.impl;

import com.zsf.container.dao.SmokeDao;
import com.zsf.container.entity.Smoke;
import com.zsf.container.service.SmokeService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ReM
 */
@Service
public class SmokeServiceImpl extends BaseServiceImpl<Smoke,Long> implements SmokeService {

    private SmokeDao smokeDao;
    @Autowired
    public void setSmokeDao(SmokeDao smokeDao) {
        this.smokeDao = smokeDao;
        this.setBaseDAO(smokeDao);
    }
}
