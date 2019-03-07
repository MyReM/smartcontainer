package com.zsf.core.controller;

import com.zsf.core.config.web.BusinessException;
import com.zsf.core.entity.Menu;
import com.zsf.core.model.MenuModel;
import com.zsf.core.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public Page findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        String parentName,
                        Menu menu){
        if (!Objects.equals(page,0)){
            page = page -1;
        }

        if (StringUtils.hasLength(parentName)) {
            Menu temp = new Menu();
            temp.setMenuName(parentName);
            ExampleMatcher matcher = ExampleMatcher.matching();
            Example<Menu> example = Example.of(temp,matcher);
            Menu temp1 = menuService.findOne(example);

            Objects.requireNonNull(temp1, "请输入正确的父级菜单名称！");

            menu.setParentId(temp1.getMenuId());
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"menuId", "sortNum"));

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Menu> example = Example.of(menu,matcher);
        Page<Menu> pages = menuService.findAll(example,pageable);

        List<MenuModel> menuModelList = new ArrayList<>();

        pages.getContent().stream().forEach(item -> {
            MenuModel model = new MenuModel();

            BeanUtils.copyProperties(item, model);

            if (Objects.nonNull(item.getParentId())){
                Menu parent = menuService.findOne(item.getParentId());

                if (Objects.nonNull(parent)){
                    model.setParent(parent);
                }
            }

            menuModelList.add(model);
        });

        Page<MenuModel> page1 = new PageImpl<>(menuModelList, pageable, pages.getTotalElements());

        return page1;
    }

    @PostMapping
    public String insert(@RequestBody Menu menu) throws Exception{

        if (Objects.nonNull(menu.getMenuId()) && Objects.nonNull(menu.getParentId()) && Objects.equals(menu.getMenuId(), menu.getParentId())) {
            throw new BusinessException("自身不能作为父级！");
        }

        if (Objects.isNull(menu.getParentId())){
            menu.setLevel(1);
        }else {
            Menu parentDept = menuService.findOne(menu.getParentId());

            Objects.requireNonNull(menu, "找不到父级部门");

            Integer level = parentDept.getLevel();
            menu.setLevel(level + 1);
        }

        menuService.save(menu);

        return "success";
    }

    @PutMapping
    public String update(@RequestBody Menu menu) throws Exception {

        Objects.requireNonNull(menu.getMenuId(), "数据有误");

        Menu m = menuService.findOne(menu.getMenuId());

        Objects.requireNonNull(m,"该菜单不存在！");

        if (m.equals(menu)){
            throw new BusinessException("未做任何更改！");
        }

        if (Objects.equals(menu.getMenuId(), menu.getParentId())) {
            throw new BusinessException("自身不能作为父级！");
        }

        if (Objects.isNull(menu.getParentId())) {
            menu.setLevel(1);
        } else {
            Menu parent = menuService.findOne(menu.getParentId());

            Objects.requireNonNull(parent, "父级不存在！");

            menu.setLevel(parent.getLevel() + 1);
        }

        menuService.save(menu);

        return "success";
    }

    @DeleteMapping(value = "/{menuId}")
    public String delete(@PathVariable(value = "menuId") Long menuId){

        Objects.requireNonNull(menuId, "数据有误");

        Menu menu = menuService.findOne(menuId);

        Objects.requireNonNull(menu,"该菜单不存在！");

        menuService.delete(menu);

        return "success";
    }

    @GetMapping(value = "/getParentId")
    public List<Long> getParentId(Long menuId) {

        Objects.requireNonNull(menuId, "菜单ID不能为空！");

        Menu menu = menuService.findOne(menuId);

        Objects.requireNonNull(menu, "该菜单不存在！");

        List<Long> list = new ArrayList<>();

        getParentId(menu, list);

        Collections.reverse(list);

        return list;
    }

    private void getParentId(Menu menu, List<Long> list) {

        Long parentId = menu.getParentId();

        if (Objects.nonNull(parentId)) {
            list.add(parentId);

            Menu parentMenu = menuService.findOne(parentId);

            getParentId(parentMenu, list);

        }
    }

    @GetMapping(value = "/tree")
    public List<MenuModel> menuTree(){

        Menu menu = new Menu();
        menu.setLevel(1);
        ExampleMatcher matcher = ExampleMatcher.matching();
        Example<Menu> example = Example.of(menu,matcher);
        List<Menu> list = menuService.findAll(example);

        return findMenuTree(list);
    }

    private List<MenuModel> findMenuTree(List<Menu> list){
        List<MenuModel> tree = new ArrayList<>();

        list.stream().forEach(item -> {
            MenuModel model = new MenuModel();
            BeanUtils.copyProperties(item, model);

            ExampleMatcher matcher = ExampleMatcher.matching();
            Menu menu = new Menu();
            menu.setParentId(item.getMenuId());
            Example<Menu> example = Example.of(menu,matcher);
            List<Menu> children = menuService.findAll(example);

            if (!ObjectUtils.isEmpty(children)){
                model.setChildren(children);
            }

            tree.add(model);

            findMenuTree(children);

        });

        return tree;
    }


}
