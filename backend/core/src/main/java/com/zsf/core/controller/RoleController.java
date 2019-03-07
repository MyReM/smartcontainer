package com.zsf.core.controller;

import com.zsf.core.config.web.BusinessException;
import com.zsf.core.entity.Menu;
import com.zsf.core.entity.Permission;
import com.zsf.core.entity.Role;
import com.zsf.core.entity.RoleMenu;
import com.zsf.core.model.MenuModel;
import com.zsf.core.model.RoleModel;
import com.zsf.core.service.MenuService;
import com.zsf.core.service.PermissionService;
import com.zsf.core.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuService menuService;

    @GetMapping(value = "/page")
    public Page findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size, Role role){

        if (!Objects.equals(page,0)){
            page = page -1;
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"roleId"));

        ExampleMatcher matcher = ExampleMatcher.matching();

        Example<Role> example = Example.of(role,matcher);
        Page<Role> pages = roleService.findAll(example,pageable);

        List<RoleModel> roleModelList = new ArrayList<>();
        pages.stream().forEach(item -> {

            RoleModel roleModel = new RoleModel();
            BeanUtils.copyProperties(item, roleModel);

            if (Objects.nonNull(item.getParentId())) {
                Role parent = roleService.findOne(item.getParentId());
                if (Objects.nonNull(parent)) {
                    roleModel.setParent(parent);
                }
            }

            roleModelList.add(roleModel);
        });

        Page rolePage = new PageImpl(roleModelList, pageable, pages.getTotalElements());

        return rolePage;
    }

    @GetMapping(value = "/tree")
    public List<RoleModel> findAll(){
        Role role = Role.builder().level(1).build();
        ExampleMatcher matcher = ExampleMatcher.matching();
        Example<Role> example = Example.of(role, matcher);

        List<Role> roleList = roleService.findAll(example);

        List<RoleModel> roleModelList = findTree(roleList);

        return roleModelList;
    }

    private List<RoleModel> findTree(List<Role> list){

        List<RoleModel> roleList = new ArrayList<>();

        list.stream().forEach(item -> {

            RoleModel deptModel = new RoleModel();

            BeanUtils.copyProperties(item, deptModel);

            Role dept = new Role();
            dept.setParentId(item.getRoleId());
            ExampleMatcher matcher = ExampleMatcher.matching();
            Example<Role> example = Example.of(dept, matcher);

            List<Role> deptList = roleService.findAll(example);

            deptModel.setChildren(deptList);

            roleList.add(deptModel);

            findTree(deptList);

        });

        return roleList;
    }

    @PostMapping
    public String insert(@RequestBody Role role){

        if (Objects.isNull(role.getParentId())){
            role.setLevel(1);
        }else {
            Role parentDept = roleService.findOne(role.getParentId());
            if (Objects.isNull(parentDept)){
                throw new NullPointerException("找不到父级部门");
            }else {
                Integer level = parentDept.getLevel();
                role.setLevel(level + 1);
            }
        }

        role.setStatus(1);
        role.setCreateTime(new Date());

        roleService.save(role);

        return "success";
    }

    @PostMapping(value = "/{roleId}/permission")
    public String allotPermission(@PathVariable(value = "roleId") Long roleId, @RequestBody List<Long> permissions){
        Role role = roleService.findOne(roleId);
        Objects.requireNonNull(role, "该角色不存在！");

        Set<Permission> permissionSet = new HashSet<>();
        permissions.stream().forEach(item -> {
            Permission permission = permissionService.findOne(item);
            permissionSet.add(permission);
        });
        role.setPermissions(permissionSet);

        roleService.save(role);
        return SUCCESS;
    }

    @PutMapping
    public String update(@RequestBody Role role) throws Exception {

        Role role1 = roleService.findOne(role.getRoleId());

        Objects.requireNonNull(role1, "未找到您所要修改的角色");

        if (role1.equals(role)) {
            throw new BusinessException("未做任何修改");
        }

        role.setCreateTime(new Date());

        roleService.save(role);

        return "success";
    }

    @GetMapping(value = "/menu/{roleId}")
    public List<Long> findMenuIdByRoleId(@PathVariable(value = "roleId") Long roleId){

        Role role = roleService.findOne(roleId);

        Objects.requireNonNull(role, "该角色不存在！");

        if (ObjectUtils.isEmpty(role.getMenuList())) {
            return new ArrayList<>();
        } else {
            return role.getMenuList().stream().map(menu -> menu.getMenuId()).collect(Collectors.toList());
        }
    }

    @PostMapping(value = "/menu/{roleId}")
    public String allotMenu(@PathVariable(value = "roleId") Long roleId, @RequestBody List<Long> menuId){

        Role role = roleService.findOne(roleId);

        Objects.requireNonNull(role, "该角色不存在！");

        Set<Menu> menuList = new HashSet<>();

        menuId.stream().forEach(id -> {
            Menu menu = menuService.findOne(id);
            if (Objects.nonNull(menu)) {
                menuList.add(menu);
            }
        });

//        List<Long> menuList = roleService.findMenuIdByRoleId(roleId);
//
//        // 取交集，两者都有，不需要更改
//        List<Long> both = menuId.stream().filter(id -> menuList.contains(id)).collect(Collectors.toList());
//
//        // 新增的菜单：新菜单集合-交集
//        List<Long> insert = menuId.stream().filter(id -> !both.contains(id)).collect(Collectors.toList());
//
//        // 移除的菜单：旧菜单集合-交集
//        List<Long> delete = menuList.stream().filter(id -> !both.contains(id)).collect(Collectors.toList());
//
//        List<RoleMenu> insertList = new ArrayList<>();
//
//        insert.stream().forEach(id -> {
//            RoleMenu roleMenu = new RoleMenu();
//            roleMenu.setRoleId(roleId);
//            roleMenu.setMenuId(id);
//            insertList.add(roleMenu);
//        });
//
//        List<RoleMenu> deleteList = new ArrayList<>();
//
//        delete.stream().forEach(id -> {
//            RoleMenu roleMenu = new RoleMenu();
//            roleMenu.setRoleId(roleId);
//            roleMenu.setMenuId(id);
//            deleteList.add(roleMenu);
//        });

        role.setMenuList(menuList);

        roleService.save(role);

        return SUCCESS;
    }

    @DeleteMapping(value = "/{roleId}")
    public String delete(@PathVariable(value = "roleId") Long roleId){

        Role role = roleService.findOne(roleId);

        Objects.requireNonNull(role, "该角色不存在");

        roleService.delete(role);

        return "success";
    }
}
