package com.zsf.container.service.impl;

import com.zsf.container.dao.AntiTypeDao;
import com.zsf.container.entity.AntiType;
import com.zsf.container.service.AntiTypeService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AntiTypeServiceImpl extends BaseServiceImpl<AntiType,Long> implements AntiTypeService {

    private AntiTypeDao antiTypeDao;

    @Autowired
    public void setAntiTypeDao(AntiTypeDao antiTypeDao) {
        this.antiTypeDao = antiTypeDao;
        this.setBaseDAO(antiTypeDao);
    }

    @Override
    public Page<AntiType> getPage(Integer page, Integer size, AntiType antiType) {
        if(!Objects.equals(page,0)) {
            --page;
        }
        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"id"));
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<AntiType> example = Example.of(antiType,exampleMatcher);
        return findAll(example,pageable);
    }
}
