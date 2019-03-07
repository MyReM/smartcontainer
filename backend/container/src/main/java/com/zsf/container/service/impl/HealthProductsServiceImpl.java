package com.zsf.container.service.impl;

import com.zsf.container.dao.HealthProductsDao;
import com.zsf.container.entity.HealthProducts;
import com.zsf.container.service.HealthProductsService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ReM
 */
@Service
public class HealthProductsServiceImpl extends BaseServiceImpl<HealthProducts,Long> implements HealthProductsService {

    private HealthProductsDao healthProductsDao;

    @Autowired
    public void setHealthProductsDao(HealthProductsDao healthProductsDao) {
        this.healthProductsDao = healthProductsDao;
        this.setBaseDAO(healthProductsDao);
    }
}
