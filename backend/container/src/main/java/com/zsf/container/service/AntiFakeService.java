package com.zsf.container.service;

import com.zsf.container.entity.AntiFake;
import com.zsf.core.service.BaseService;
import org.springframework.data.domain.Page;

public interface AntiFakeService extends BaseService<AntiFake,Long>{

    Page<AntiFake> getPages(Integer page,Integer size,AntiFake antiFake);
}
