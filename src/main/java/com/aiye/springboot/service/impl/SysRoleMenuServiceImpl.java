package com.aiye.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiye.springboot.entity.SysRole;
import com.aiye.springboot.entity.SysRoleMenu;
import com.aiye.springboot.mapper.SysRoleMenuMapper;
import com.aiye.springboot.service.ISysRoleMenuService;
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
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

}
