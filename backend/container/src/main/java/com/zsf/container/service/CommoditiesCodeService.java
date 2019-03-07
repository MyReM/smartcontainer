package com.zsf.container.service;

import com.zsf.container.entity.CommoditiesCode;
import com.zsf.core.service.BaseService;
import org.springframework.data.domain.Page;

/**
 * @author ReM
 */
public interface CommoditiesCodeService extends BaseService<CommoditiesCode,Long> {

    Page<CommoditiesCode> getPage(Integer page,Integer size,CommoditiesCode commoditiesCode);

    Integer countByTypeAndIsInOrType(Integer type,Integer isIn);
}
