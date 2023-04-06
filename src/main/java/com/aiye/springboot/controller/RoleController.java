package com.aiye.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.aiye.springboot.common.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.aiye.springboot.service.ISysRoleService;
import com.aiye.springboot.entity.SysRole;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author aiye
 * @since 2022-12-18
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @PostMapping//保存，新增修改
    public Result save(@RequestBody SysRole sysRole) {
        sysRoleService.saveOrUpdate(sysRole);
        return Result.success();
    }

    @DeleteMapping("/{id}")//删除
    public Result delete(@PathVariable Integer id) {
        sysRoleService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")//删除多个
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sysRoleService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping//查询所有
    public Result findAll() {
        List<SysRole> list = sysRoleService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")//id查询
    public Result findOne(@PathVariable Integer id) {
        return Result.success(sysRoleService.getById(id));
    }

    @GetMapping("/page")//分页查询
    public Result findPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize,@RequestParam String name) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByAsc("id");
        return Result.success( sysRoleService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }

    /**
     * 绑定角色和菜单的关系
     * @param
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result saveRoleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds) {
        sysRoleService.setRoleMenu(roleId,menuIds);
        return Result.success();
    }
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId ) {
        return Result.success(sysRoleService.getRoleMenu(roleId));
    }

}

