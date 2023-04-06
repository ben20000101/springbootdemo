package com.aiye.springboot.mapper;

import com.aiye.springboot.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aiye
 * @since 2022-12-18
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select id from sys_role where flag =#{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}
