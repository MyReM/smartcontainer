package com.zsf.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yyq
 */
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = -5699775009305977526L;

    private Long userId;

    private Long roleId;


}
