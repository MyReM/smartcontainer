package com.zsf.core.mapper;

import com.zsf.core.entity.Menu;
import com.zsf.core.entity.RoleMenu;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yyq
 */
@Mapper
@Component
public interface RoleMenuMapper {

    @Insert(value = "insert into sys_role_menu(role_id, menu_id) values(#{roleId}, #{menuId})")
    void save(RoleMenu roleMenu);

//    @InsertProvider(type = RoleMenuMapperProvider.class, method = "save")
//    void saveAll(List<RoleMenu> roleMenuList);

//    @DeleteProvider(type = RoleMenuMapperProvider.class, method = "delete")
//    int delete(List<RoleMenu> roleMenuList);

    @Delete(value = "delete from sys_role_menu where role_id = #{roleId} and menu_id = #{menuId}")
    int delete(RoleMenu roleMenu);

    @Select(value = "select * from sys_menu where menu_id in (select menu_id from sys_role_menu where role_id = #{roleId}) and level = 1 order by sort_num")
    List<Menu> findParentMenu(Long roleId);

    @Select(value = "select count(*) from sys_role_menu where role_id = #{roleId} and menu_id = #{menuId}")
    Long findByRoleIdAndMenuId(@Param(value = "roleId") Long roleId, @Param(value = "menuId") Long menuId);


    class RoleMenuMapperProvider{


//        public String save(List<RoleMenu> roleMenuList){
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            roleMenuList.stream().forEach(roleMenu -> {
//                stringBuilder.append("insert into role_menu(role_id, menu_id) ");
//                stringBuilder.append("values(#{roleId}, #{menuId})");
//            });
//
//            return stringBuilder.toString();
//        }
//
//        public String delete(List<RoleMenu> roleMenuList){
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            stringBuilder.append("delete from role_menu where ");
//
//            int size = roleMenuList.size();
//
//            for (int i = 0; i < size; i++){
//                if (i == size -1){
//                    stringBuilder.append("(role_id = #{roleId} and menu_id = #{menuId})");
//                } else {
//                    stringBuilder.append("(role_id = #{roleId} and menu_id = #{menuId}) or ");
//                }
//            }
//
//            return stringBuilder.toString();
//        }

    }
 }
