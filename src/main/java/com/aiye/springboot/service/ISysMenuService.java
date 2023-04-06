package com.aiye.springboot.service;

import com.aiye.springboot.entity.SysMenu;
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
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> findMenus(String name);
}
