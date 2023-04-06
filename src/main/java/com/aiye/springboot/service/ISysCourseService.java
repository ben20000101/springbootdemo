package com.aiye.springboot.service;

import com.aiye.springboot.entity.SysCourse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aiye
 * @since 2022-12-27
 */
public interface ISysCourseService extends IService<SysCourse> {

    Page<SysCourse> findPage(Page<SysCourse> objectPage,@Param("name") String name);

    void setStudentCourse(@Param("courseId") Integer courseId,@Param("studentId") Integer studentId);
}
