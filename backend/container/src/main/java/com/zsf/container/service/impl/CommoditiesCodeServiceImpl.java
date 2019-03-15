package com.zsf.container.service.impl;

import com.zsf.container.dao.CommoditiesCodeDao;
import com.zsf.container.entity.CommoditiesCode;
import com.zsf.container.service.CommoditiesCodeService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author ReM
 */
@Service
public class CommoditiesCodeServiceImpl extends BaseServiceImpl<CommoditiesCode,Long> implements CommoditiesCodeService {

    private CommoditiesCodeDao commoditiesCodeDao;

    @Autowired
    public void setCommodityDao(CommoditiesCodeDao commoditiesCodeDao) {
        this.commoditiesCodeDao = commoditiesCodeDao;
        this.setBaseDAO(commoditiesCodeDao);
    }

    @Override
    public Page<CommoditiesCode> getPage(Integer page, Integer size, CommoditiesCode commoditiesCode) {

        if(!Objects.equals(page,0)) {
            --page;
        }
        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"id"));
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<CommoditiesCode> example = Example.of(commoditiesCode,exampleMatcher);
        return findAll(example,pageable);
    }

    @Override
    public Integer countByTypeAndIsInOrType(Integer type, Integer isIn) {
        CommoditiesCode commoditiesCode = new CommoditiesCode();
        if (isIn != null) {
            commoditiesCode.setIsIn(isIn);
        }
        if (type != null) {
            commoditiesCode.setCommoditiesType(type);
        }
        Example<CommoditiesCode> example = Example.of(commoditiesCode);
        List<CommoditiesCode> codes = findAll(example);
        return codes.size();
    }

}
