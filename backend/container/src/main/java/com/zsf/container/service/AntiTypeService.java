package com.zsf.container.service;

import com.zsf.container.entity.AntiType;
import com.zsf.core.service.BaseService;
import org.springframework.data.domain.Page;

public interface AntiTypeService extends BaseService<AntiType,Long> {

    Page<AntiType> getPage(Integer page, Integer size, AntiType antiType);
}
