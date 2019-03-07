package com.zsf.core.model;

import com.zsf.core.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @author yyq
 */
@Data
public class RoleModel extends Role {

    private Role parent;

    private List<Role> children;

}
