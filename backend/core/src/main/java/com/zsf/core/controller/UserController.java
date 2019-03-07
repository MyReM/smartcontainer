package com.zsf.core.controller;
import com.zsf.core.config.web.BusinessException;
import com.zsf.core.config.web.Message;
import com.zsf.core.entity.*;
import com.zsf.core.service.*;
import com.zsf.core.utils.EndecryptUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private LogService logService;

    @GetMapping(value = "/error")
    public Message loginFaild(){
        Message message = Message.builder().code(Message.Code.UNAUTH).message("登录失败，请重新登录").build();
        return message;
    }

    @PostMapping(value = "/login")
    public Message login(@RequestBody Map<String, Object> userMap){
        String userCode = userMap.get("userCode").toString();
        String password = userMap.get("password").toString();

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(userCode, password);

        token.setRememberMe(false);

        Message message = new Message();

        Log log = Log.builder().type(Log.LogType.LOGIN).createTime(new Date()).build();

        try {
            subject.login(token);
            Object sessionId =  subject.getSession().getId();

            Map<String, Object> map = new HashMap<>();
            map.put("token", sessionId);

            message.setCode(Message.Code.SUCCESS);
            message.setMessage("success");
            message.setData(map);

            log.setLevel(Log.LogLevel.INFO);
            log.setMessage(userCode + "：登录成功");

        } catch (UnknownAccountException e) {
            message.setCode(Message.Code.ERROR);
            message.setMessage("账号不存在");

            log.setLevel(Log.LogLevel.ERROR);
            log.setMessage(userCode + "：账号不存在");


        } catch (LockedAccountException e) {
            message.setCode(Message.Code.ERROR);
            message.setMessage("账户被锁定");

            log.setLevel(Log.LogLevel.ERROR);
            log.setMessage(userCode + "：账户被锁定");

        } catch (AuthenticationException e) {
            message.setCode(Message.Code.ERROR);
            message.setMessage("认证失败");

            log.setLevel(Log.LogLevel.ERROR);
            log.setMessage(userCode + "：认证失败");
        }

        logService.save(log);

        return message;

    }

    @GetMapping(value = "/index")
    public Map<String,Object> userInfo(String token) {
        Map<String,Object> map = new HashMap<>();

        User user = userService.findOne(getUserInfo().getUserId());

        Role role = roleService.findOne(user.getRoleId());

        Objects.requireNonNull(role, "角色不能为空！");

        map.put("roles", role.getRoleName());

        Set<Permission> permissionSet = role.getPermissions();

        List<String> permissions = permissionSet.stream().map(Permission::getPermissionCode).collect(Collectors.toList());

        map.put("permissions",permissions);

        map.put("userId", user.getUserId());

        map.put("userCode", user.getUserCode());

        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return map;
    }

    @GetMapping
    public Page findAll(@RequestParam(name = "page",defaultValue = "0") Integer page,
                        @RequestParam(name = "size",defaultValue = "10") Integer size,
                        User user){

        if (!Objects.equals(page,0)){
            page = page -1;
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"userId"));

        ExampleMatcher matcher = ExampleMatcher.matching();

        Example<User> example = Example.of(user,matcher);
        Page pages = userService.findAll(example,pageable);

        return pages;
    }

    @GetMapping(value = "/list")
    public List<User> findAll(User user) {

        ExampleMatcher matcher = ExampleMatcher.matching();

        Example<User> example = Example.of(user,matcher);

        List<User> list = userService.findAll(example);

        return list;
    }

    @GetMapping(value = "/{userId}")
    public User findOne(@PathVariable(value = "userId") Long userId){

        Objects.requireNonNull(userId, "数据有误");

        User user = userService.findOne(userId);

        Objects.requireNonNull(user,"用户不存在！");

        return user;
    }

    @PostMapping
    public void insert(@RequestBody User user){

        user.setSalt(user.getPassword());

        String password = EndecryptUtil.encryt(user.getPassword());

        user.setPassword(password);

        user.setCreateTime(new Date());

        user.setStatus(User.Status.NORMAL);

        userService.save(user);
    }

    @GetMapping(value = "/exist")
    public Boolean exist(String userCode){

        User user = userService.findByUserCode(userCode);

        return Objects.nonNull(user);
    }

    @PutMapping
    public String update(@RequestBody User user) throws Exception {

        Objects.requireNonNull(user.getUserId(), "数据有误");

        User u = userService.findOne(user.getUserId());

        Objects.requireNonNull(u,"用户不存在！");

        if (u.equals(user)){
            throw new BusinessException("未做任何更改");
        }

        userService.save(user);

        return "success";
    }

    @DeleteMapping(value = "/{userId}")
    public String delete(@PathVariable(value = "userId") Long userId){

        User user = userService.findOne(userId);

        Objects.requireNonNull(user,"用户不存在！");

        userService.delete(user);

        return "success";
    }

    @GetMapping(value = "/deptId/{deptId}")
    public Page<User> findByDeptId(
            @PathVariable(value = "deptId") Long deptId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size, User user){

        if (!Objects.equals(page,0)){
            page = page -1;
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"userId"));

        ExampleMatcher matcher = ExampleMatcher.matching();

        user.setDeptId(deptId);

        Example<User> example = Example.of(user,matcher);

        Page pages = userService.findAll(example,pageable);

        return pages;
    }

    @GetMapping(value = "/roleId/{roleId}")
    public Page<User> findByRoleId(
            @PathVariable(value = "roleId") Long roleId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size, User user){

        if (!Objects.equals(page,0)){
            page = page -1;
        }

        Pageable pageable = PageRequest.of(page,size,new Sort(Sort.Direction.DESC,"userId"));

        ExampleMatcher matcher = ExampleMatcher.matching();

        user.setRoleId(roleId);

        Example<User> example = Example.of(user,matcher);

        Page pages = userService.findAll(example,pageable);

        return pages;
    }

    @GetMapping(value = "/dept/{userId}")
    public Dept findDeptByUserId(@PathVariable(value = "userId") Long userId){
        Objects.requireNonNull(userId, "您传入的数据有误，请检查");

        User user = userService.findOne(userId);

        Objects.requireNonNull(user, "该用户不存在");

        if (Objects.isNull(user.getDeptId())){
            return null;
        }

        Dept dept = deptService.findOne(user.getDeptId());

        Objects.requireNonNull(user, "该用户的部门不存在");

        return dept;

    }

    @GetMapping(value = "/role/{userId}")
    public Role findRoleByUserId(@PathVariable(value = "userId") Long userId){
        Objects.requireNonNull(userId, "您传入的数据有误，请检查");

        User user = userService.findOne(userId);

        Objects.requireNonNull(user, "该用户不存在");

        if (Objects.isNull(user.getRoleId())){
            return null;
        }

        Role role = roleService.findOne(user.getRoleId());

        Objects.requireNonNull(user, "该用户角色不存在");

        return role;
    }

    @PutMapping(value = "/{userId}/{deptId}")
    public String updateDept(@PathVariable(value = "userId") Long userId, @PathVariable(value = "deptId") Long deptId){

        if (Objects.isNull(userId) || Objects.isNull(deptId)){
            throw new NullPointerException("输入的数据有误，请检查");
        }

        User user = userService.findOne(userId);

        Objects.requireNonNull(user, "该用户不存在");

        user.setDeptId(deptId);

        userService.save(user);

        return "success";
    }

    @PutMapping(value = "/role")
    public String updateRole(@RequestBody Map<String, Long> map){

        Long userId = map.get("userId");
        Long roleId = map.get("roleId");

        if (Objects.isNull(userId) || Objects.isNull(roleId)){
            throw new NullPointerException("输入的数据有误，请检查");
        }

        User user = userService.findOne(userId);

        Objects.requireNonNull(user, "该用户不存在");

        user.setRoleId(roleId);

        userService.save(user);

        return "success";
    }


    @PostMapping(value = "/logout")
    public String logout(){

        return "成功注销！";
    }


}
