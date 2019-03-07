package com.zsf.container.service.impl;

import com.zsf.container.dao.AntiFakeDao;
import com.zsf.container.entity.AntiFake;
import com.zsf.container.service.AntiFakeService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AntiFakeServiceImpl extends BaseServiceImpl<AntiFake,Long> implements AntiFakeService {

    private AntiFakeDao antiFakeDao;

    @Autowired
    public void setDao(AntiFakeDao antiFakeDao) {
        this.antiFakeDao = antiFakeDao;
        this.setBaseDAO(antiFakeDao);
    }

    @Override
    public Page<AntiFake> getPages(Integer page, Integer size, AntiFake antiFake) {

        if(!Objects.equals(page,0)) {
            --page;
        }
        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"id"));
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<AntiFake> example1 = Example.of(antiFake,exampleMatcher);
        return findAll(example1,pageable);
    }
}
