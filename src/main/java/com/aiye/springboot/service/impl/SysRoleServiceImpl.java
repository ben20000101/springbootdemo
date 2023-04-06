package com.aiye.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aiye.springboot.entity.SysMenu;
import com.aiye.springboot.entity.SysRole;
import com.aiye.springboot.entity.SysRoleMenu;
import com.aiye.springboot.mapper.SysRoleMapper;
import com.aiye.springboot.mapper.SysRoleMenuMapper;
import com.aiye.springboot.service.ISysMenuService;
import com.aiye.springboot.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aiye
 * @since 2022-12-18
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private ISysMenuService menuService;
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds){
//        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id",roleId);
//        roleMenuMapper.delete(queryWrapper);
        //先删除当前角色id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleId);
        //把前端传过来的菜单id数组绑定到当前这个角色id上
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for(Integer menuId : menuIds){
            SysMenu menu = menuService.getById(menuId);
            //二级菜单 传过来的menuids中没有父级id，添加
            if(menu.getPid()!=null && !menuIdsCopy.contains(menu.getPid())){
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setMenuId(menu.getPid());
                roleMenu.setRoleId(roleId);
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }

            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
