package com.zsf.container.service.impl;

import com.zsf.container.dao.DrinksDao;
import com.zsf.container.entity.Drinks;
import com.zsf.container.service.DrinksService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ReM
 */
@Service
public class DrinksServiceImpl extends BaseServiceImpl<Drinks,Long> implements DrinksService {

    private DrinksDao drinksDao;

    @Autowired
    public void setDrinksDao(DrinksDao drinksDao){
        this.drinksDao = drinksDao;
        this.setBaseDAO(drinksDao);
    }
}
