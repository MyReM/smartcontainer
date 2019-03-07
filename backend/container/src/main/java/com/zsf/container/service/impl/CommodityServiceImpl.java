package com.zsf.container.service.impl;

import com.zsf.container.dao.CommodityDao;
import com.zsf.container.entity.Commodity;
import com.zsf.container.service.CommodityService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author ReM
 */
@Service
public class CommodityServiceImpl extends BaseServiceImpl<Commodity,Long> implements CommodityService {

    private CommodityDao commodityDao;

    @Autowired
    public void setCommodityDao(CommodityDao commodityDao) {
        this.commodityDao = commodityDao;
        this.setBaseDAO(commodityDao);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @param commodity
     * @return
     */
    @Override
    public Page<Commodity> getPage(Integer page, Integer size, Commodity commodity) {
        if (!Objects.equals(page,0)) {
            --page;
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"id"));
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Commodity> example = Example.of(commodity,exampleMatcher);
        return findAll(example,pageable);
    }

    @Override
    public Integer saveSumAndName(Integer sum,Integer totalNumber, String name, Integer commoditiesType) {

        Commodity commodity = new Commodity();
        commodity.setCommoditiesType(commoditiesType);
        Example<Commodity> example1 = Example.of(commodity);
        commodity = findOne(example1);
        commodity.setSum(sum);
        commodity.setCommoditiesName(name);
        commodity.setTotalNumber(totalNumber);
        return save(commodity) != null ? 1 : 0;
    }
}
