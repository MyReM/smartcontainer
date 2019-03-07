package com.zsf.core.controller;

import com.zsf.core.config.web.BusinessException;
import com.zsf.core.entity.Resource;
import com.zsf.core.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;


    @GetMapping
    public Page<Resource> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size, Resource resource){

        if (!Objects.equals(page,0)){
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"resourceId"));

        ExampleMatcher matcher = ExampleMatcher.matching();

        Example<Resource> example = Example.of(resource,matcher);
        Page<Resource> pages = resourceService.findAll(example,pageable);

        return pages;
    }

    @PostMapping
    public void insert(@RequestBody Resource resource){

        resourceService.save(resource);
    }

    @DeleteMapping(value = "/{resourceId}")
    public String delete(@PathVariable(value = "permissionId") Long resourceId) throws Exception {

        Objects.requireNonNull(resourceId, "数据有误");

        Resource resource = resourceService.findOne(resourceId);
        if (Objects.isNull(resource)){
            throw new BusinessException("该权限不存在");
        } else {
            resourceService.delete(resource);
        }

        return "success";
    }
}
