package com.aiye.springboot.service.impl;

import com.aiye.springboot.entity.SysCourse;
import com.aiye.springboot.mapper.SysCourseMapper;
import com.aiye.springboot.service.ISysCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aiye
 * @since 2022-12-27
 */
@Service
public class SysCourseServiceImpl extends ServiceImpl<SysCourseMapper, SysCourse> implements ISysCourseService {

    @Resource
    private SysCourseMapper courseMapper;

    @Override
    public Page<SysCourse> findPage(Page<SysCourse> page, String name) {
        return courseMapper.findPage(page,name);
    }

    @Transactional
    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.delStudentCourse(courseId,studentId);
        courseMapper.insStudentCourse(courseId,studentId);
    }
}
