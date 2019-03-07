package com.zsf.core.controller;

import com.zsf.core.config.web.BusinessException;
import com.zsf.core.entity.Dept;
import com.zsf.core.model.DeptModel;
import com.zsf.core.service.DeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping
    public Page<DeptModel> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size, Dept dept){

        if (!Objects.equals(page,0)){
            page = page -1;
        }

        Pageable pageable = PageRequest.of(page,size);

        ExampleMatcher matcher = ExampleMatcher.matching();
        Example<Dept> example = Example.of(dept, matcher);

        Page<Dept> deptPage = deptService.findAll(example, pageable);

        List<DeptModel> deptModelList = new ArrayList<>();

        deptPage.getContent().stream().forEach(item -> {

            DeptModel deptModel = new DeptModel();
            BeanUtils.copyProperties(item, deptModel);

            if (Objects.nonNull(item.getParentId())) {
                Dept parent = deptService.findOne(item.getParentId());
                if (Objects.nonNull(parent)) {
                    deptModel.setParent(parent);
                }
            }
            deptModelList.add(deptModel);

        });

        Page<DeptModel> modelPage = new PageImpl(deptModelList, pageable, deptPage.getTotalElements());

        return modelPage;
    }

    @PostMapping
    public String insert(@RequestBody Dept dept){

        if (Objects.isNull(dept.getParentId())){
            dept.setLevel(1);
        }else {
            Dept parentDept = deptService.findOne(dept.getParentId());
            if (Objects.isNull(parentDept)){
                throw new NullPointerException("找不到父级部门");
            }else {
                Integer level = parentDept.getLevel();
                dept.setLevel(level + 1);
            }
        }

        dept.setStatus(1);

        deptService.save(dept);

        return "success";
    }

    @PutMapping
    public String update(@RequestBody Dept dept) throws Exception {

        Objects.requireNonNull(dept.getDeptId(), "数据有误");

        Dept dept1 = deptService.findOne(dept.getDeptId());

        Objects.requireNonNull(dept1, "该部门不存在");

        if (Objects.equals(dept.getParentId(), dept1.getDeptId())) {
            throw new BusinessException("自身不能成为父部门");
        }

        if (dept1.equals(dept)){
            throw new BusinessException("未做任何更改");
        }

        if (!Objects.equals(dept.getParentId(), dept1.getParentId())) {

            if (Objects.isNull(dept.getParentId())) {
                dept.setLevel(1);
            } else {
                Dept dept2 = deptService.findOne(dept.getParentId());
                dept.setLevel(dept2.getLevel() + 1);
            }

        }

        deptService.save(dept);

        return "success";
    }

    @GetMapping(value = "/{deptId}")
    public Dept findOne(@PathVariable(name = "deptId") Long deptId){

        Dept dept = deptService.findOne(deptId);

        Objects.requireNonNull(dept, "该部门不存在！");

        return dept;
    }

    @DeleteMapping(value = "/{deptId}")
    public String delete(@PathVariable(name = "deptId") Long deptId){

        Dept dept = deptService.findOne(deptId);

        Objects.requireNonNull(dept, "该部门不存在！");

        deptService.delete(dept);

        return "success";
    }

    @GetMapping(value = "/tree")
    public List<DeptModel> tree(){
        Dept dept = new Dept();
        dept.setLevel(1);
        ExampleMatcher matcher = ExampleMatcher.matching();
        Example<Dept> example = Example.of(dept, matcher);

        List<Dept> deptList = deptService.findAll(example);

        List<DeptModel> deptModelList = findTree(deptList);

        return deptModelList;
    }

    private List<DeptModel> findTree(List<Dept> list){

        List<DeptModel> deptModelList = new ArrayList<>();

        list.stream().forEach(item -> {

            DeptModel deptModel = new DeptModel();

            BeanUtils.copyProperties(item, deptModel);

            Dept dept = new Dept();
            dept.setParentId(item.getDeptId());
            ExampleMatcher matcher = ExampleMatcher.matching();
            Example<Dept> example = Example.of(dept, matcher);

            List<Dept> deptList = deptService.findAll(example);

            deptModel.setChildren(deptList);

            deptModelList.add(deptModel);

            findTree(deptList);

        });

        return deptModelList;
    }
}
