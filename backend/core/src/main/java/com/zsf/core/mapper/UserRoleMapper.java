package com.zsf.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yyq
 */
@Mapper
@Component
public interface UserRoleMapper {

    @Select(value = "select roleId from sys_user_role where userId = #{userId}")
    List<Long> findRoleIdByUserId(Long userId);

    @Select(value = "select userId from sys_user_role where roleId = #{roleId}")
    List<Long> findUserIdByRoleId(Long roleId);

}
