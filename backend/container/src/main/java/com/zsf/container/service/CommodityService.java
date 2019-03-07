package com.zsf.container.service;

import com.zsf.container.entity.Commodity;
import com.zsf.core.service.BaseService;
import org.springframework.data.domain.Page;

/**
 * @author ReM
 */
public interface CommodityService extends BaseService<Commodity,Long> {

    Page<Commodity> getPage(Integer page,Integer size,Commodity commodity);

    Integer saveSumAndName(Integer sum,Integer totalNumber, String name, Integer commoditiesType);
}
