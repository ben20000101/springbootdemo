package com.aiye.springboot.service;

import com.aiye.springboot.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aiye
 * @since 2022-12-18
 */
public interface ISysRoleService extends IService<SysRole> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
