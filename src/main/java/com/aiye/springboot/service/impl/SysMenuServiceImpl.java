package com.aiye.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiye.springboot.entity.SysMenu;
import com.aiye.springboot.mapper.SysMenuMapper;
import com.aiye.springboot.service.ISysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aiye
 * @since 2022-12-18
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> findMenus(String name) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        List<SysMenu> list = list(queryWrapper);
        //找出pid为null的一级菜单
        List<SysMenu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //寻找一级菜单的子菜单
        for(SysMenu menu : parentNodes){
            //筛选所有数据中pid==父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}
