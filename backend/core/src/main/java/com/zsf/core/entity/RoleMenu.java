package com.zsf.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yyq
 */
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 5398252933456064210L;

    private Long roleId;

    private Long menuId;

}
