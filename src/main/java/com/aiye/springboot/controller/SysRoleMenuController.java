package com.aiye.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aiye.springboot.common.Result;
import com.aiye.springboot.service.ISysRoleMenuService;
import com.aiye.springboot.entity.SysRoleMenu;

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
@RequestMapping("/sys-role-menu")
public class SysRoleMenuController {

    @Resource
    private ISysRoleMenuService sysRoleMenuService;

    @PostMapping//保存，新增修改
    public Result save(@RequestBody SysRoleMenu sysRoleMenu) {
        sysRoleMenuService.saveOrUpdate(sysRoleMenu);
        return Result.success();
    }

    @DeleteMapping("/{id}")//删除
    public Result delete(@PathVariable Integer id) {
        sysRoleMenuService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")//删除多个
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sysRoleMenuService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping//查询所有
    public Result findAll() {
        return Result.success(sysRoleMenuService.list());
    }

    @GetMapping("/{id}")//id查询
    public Result findOne(@PathVariable Integer id) {
        return Result.success(sysRoleMenuService.getById(id));
    }

    @GetMapping("/page")//分页查询
    public Result findPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize) {
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        return Result.success( sysRoleMenuService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }

}

