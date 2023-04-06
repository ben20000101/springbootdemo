package com.aiye.springboot.controller;


import com.aiye.springboot.entity.User;
import com.aiye.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aiye.springboot.common.Result;
import com.aiye.springboot.service.ISysCourseService;
import com.aiye.springboot.entity.SysCourse;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author aiye
 * @since 2022-12-27
 */
@RestController
@RequestMapping("/course")
public class SysCourseController {

    @Resource
    private ISysCourseService sysCourseService;
    private IUserService iUserService;

    @PostMapping//保存，新增修改
    public Result save(@RequestBody SysCourse sysCourse) {
        sysCourseService.saveOrUpdate(sysCourse);
        return Result.success();
    }

    @PostMapping("/studentCourse/{studentId}/{courseId}")//保存，新增修改
    public Result studentCourse(@PathVariable Integer courseId,@PathVariable Integer studentId) {
        sysCourseService.setStudentCourse(courseId,studentId);
        return Result.success();
    }

    @DeleteMapping("/{id}")//删除
    public Result delete(@PathVariable Integer id) {
        sysCourseService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")//删除多个
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sysCourseService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping//查询所有
    public Result findAll() {
        return Result.success(sysCourseService.list());
    }

    @GetMapping("/{id}")//id查询
    public Result findOne(@PathVariable Integer id) {
        return Result.success(sysCourseService.getById(id));
    }

    @GetMapping("/page")//分页查询
    public Result findPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize,@RequestParam String name) {
//        QueryWrapper<SysCourse> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByAsc("id");
//        Page<SysCourse> page = sysCourseService.page(new Page<>(pageNum, pageSize),queryWrapper);
//        List<SysCourse> records = page.getRecords();
//        for(SysCourse record: records){
//            User user = iUserService.getById(record.getTeacherId() );
//            if (user != null) {
//                record.setTeacher(user.getNickname());
//            }
//        }
        Page<SysCourse> page = sysCourseService.findPage(new Page<>(pageNum,pageSize),name);
        return Result.success(page);
    }

}

