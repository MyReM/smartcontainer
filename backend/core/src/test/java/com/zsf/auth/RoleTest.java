package com.zsf.auth;

import com.zsf.core.entity.Role;
import org.junit.Test;

public class RoleTest {

    @Test
    public void test(){
        Role role = Role.builder().roleId(1L).roleName("test").build();

        Role role1 = Role.builder().roleId(1L).roleName("test").build();

        System.out.println(role.equals(role1));
    }
}
