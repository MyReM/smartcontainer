package com.zsf.core.controller;

import com.zsf.core.config.web.BusinessException;
import com.zsf.core.entity.Permission;
import com.zsf.core.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/permission")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/page")
    public Page<Permission> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size, Permission permission){

        if (!Objects.equals(page,0)){
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"permissionId"));

        ExampleMatcher matcher = ExampleMatcher.matching();

        Example<Permission> example = Example.of(permission,matcher);
        Page<Permission> pages = permissionService.findAll(example,pageable);

        return pages;
    }

    @GetMapping(value = "/list")
    public List<Permission> findAll(){
        return permissionService.findAll();
    }

    @PostMapping
    public void insert(@RequestBody Permission permission){

        permissionService.save(permission);
    }

    @PutMapping
    public String update(@RequestBody Permission permission) throws Exception {

        Permission permission1 = permissionService.findOne(permission.getPermissionId());

        Objects.requireNonNull(permission1, "权限不存在！");

        if (permission1.equals(permission)) {
            throw new BusinessException("您未做修改！");
        }

        permissionService.save(permission);

        return SUCCESS;
    }

    @DeleteMapping(value = "/{permissionId}")
    public String delete(@PathVariable(value = "permissionId") Long permissionId) throws Exception {

        Objects.requireNonNull(permissionId, "数据有误");

        Permission permission = permissionService.findOne(permissionId);
        if (Objects.isNull(permission)){
            throw new BusinessException("该权限不存在");
        } else {
            permissionService.delete(permission);
        }

        return "success";
    }

}
