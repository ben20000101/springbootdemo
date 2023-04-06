package com.aiye.springboot.controller;


import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.aiye.springboot.common.Constants;
import com.aiye.springboot.common.Result;
import com.aiye.springboot.entity.SysDict;
import com.aiye.springboot.mapper.DictMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.aiye.springboot.service.ISysMenuService;
import com.aiye.springboot.entity.SysMenu;

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
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private ISysMenuService sysMenuService;
    @Resource
    private DictMapper dictMapper;

    @PostMapping//保存，新增修改
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.saveOrUpdate(sysMenu);
        return Result.success();
    }

    @DeleteMapping("/{id}")//删除
    public Result delete(@PathVariable Integer id) {
        sysMenuService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")//删除多个
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sysMenuService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping//查询所有
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        List<SysMenu> list = sysMenuService.findMenus(name);
        return Result.success(list);
    }

    @GetMapping("/{id}")//id查询
    public Result findOne(@PathVariable Integer id) {
        return Result.success(sysMenuService.getById(id));
    }

    @GetMapping("/page")//分页查询
    public Result findPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize,@RequestParam String name) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        return Result.success( sysMenuService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }

    @GetMapping("/icons")//id查询
    public Result getIcons() {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.Dict_TYPE_ICON);
        List<SysDict> list = dictMapper.selectList(queryWrapper);
        return Result.success(list);
    }


    @GetMapping("/ids")
    public Result findAllIds(){
        return Result.success(sysMenuService.list().stream().map(SysMenu::getId));
    }
}

