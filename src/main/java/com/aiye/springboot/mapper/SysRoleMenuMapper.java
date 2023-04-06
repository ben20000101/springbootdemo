package com.aiye.springboot.mapper;

import com.aiye.springboot.entity.SysRole;
import com.aiye.springboot.entity.SysRoleMenu;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aiye
 * @since 2022-12-18
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Delete("delete from sys_role_menu where role_id =#{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    @Select("select menu_id from sys_role_menu where role_id =#{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
