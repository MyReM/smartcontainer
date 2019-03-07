package com.zsf.core.controller;

import com.zsf.core.config.shiro.UserInfo;
import com.zsf.core.config.web.BusinessException;
import com.zsf.core.entity.Menu;
import com.zsf.core.entity.Role;
import com.zsf.core.entity.User;
import com.zsf.core.model.MenuModel;
import com.zsf.core.service.MenuService;
import com.zsf.core.service.RoleService;
import com.zsf.core.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/system")
public class SystemController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;


    /*****************************************角色的菜单树*************************************/

    @GetMapping(value = "/tree")
    public List<MenuModel> systemTree() throws Exception{

        UserInfo userInfo = getUserInfo();

        Long userId = userInfo.getUserId();

        Objects.requireNonNull(userId, "用户ID为空！");

        User user = userService.findOne(userId);

        Objects.requireNonNull(user, "该用户不存在！");

        Long roleId = user.getRoleId();

        Role role = roleService.findOne(roleId);

        Objects.requireNonNull(role, "找不到该角色！");

        Set<Menu> menuList = role.getMenuList();


        return this.findMenuTree(menuList, 1);

    }

    private List<MenuModel> findMenuTree(Set<Menu> list, int level){

        List<MenuModel> tree = new ArrayList<>();

        list.stream().sorted(Comparator.comparing(Menu::getSortNum)).filter(item -> Objects.equals(item.getLevel(), level)).forEach(item -> {

            MenuModel model = new MenuModel();
            BeanUtils.copyProperties(item, model);

            Set<Menu> childrens = list.stream().filter(m -> m.getLevel() > level && Objects.equals(m.getParentId(), item.getMenuId())).collect(Collectors.toSet());

            List<Menu> children = childrens.stream().sorted(Comparator.comparing(Menu::getSortNum)).filter(m -> Objects.equals(m.getLevel(), level + 1)).collect(Collectors.toList());

            if (!ObjectUtils.isEmpty(childrens)) {

                model.setChildren(children);

                findMenuTree(childrens, level + 1);
            }

            tree.add(model);

        });

        return tree;
    }

//    private List<MenuModel> findMenuTree(List<Menu> list){
//        List<MenuModel> tree = new ArrayList<>();
//
//        list.stream().forEach(item -> {
//
//            MenuModel model = new MenuModel();
//            BeanUtils.copyProperties(item, model);
//
//            ExampleMatcher matcher = ExampleMatcher.matching();
//            Menu menu = new Menu();
//            menu.setParentId(item.getMenuId());
//            Example<Menu> example = Example.of(menu,matcher);
//            List<Menu> children = menuService.findAll(example);
//
//            if (!ObjectUtils.isEmpty(children)){
//                model.setChildren(children);
//                findMenuTree(children);
//            }
//
//            tree.add(model);
//
//        });
//
//        return tree;
//    }

    /**************************************************************************/

    @GetMapping(value = "/test")
    public List<Object> test(HttpServletRequest requet){
        List<Object> list = new ArrayList<>();

        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                Map<String, Object> map = new HashMap<>();

                NetworkInterface ni = netInterfaces.nextElement();
//                System.out.println("DisplayName:" + ni.getDisplayName());
//                System.out.println("Name:" + ni.getName());
                map.put("interfaceName", ni.getName());

                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
//                    System.out.println("IP:" + ips.nextElement().getHostAddress());
                    String ip = ips.nextElement().getHostAddress();
                    map.put("server_ip", ip);
                    if (ip.split(".").length > 0){

                    }
                }

                map.put("server_port", requet.getLocalPort());
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }

}
