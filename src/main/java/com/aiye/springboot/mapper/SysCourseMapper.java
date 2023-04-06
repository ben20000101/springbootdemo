package com.aiye.springboot.mapper;

import com.aiye.springboot.entity.SysCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aiye
 * @since 2022-12-27
 */
public interface SysCourseMapper extends BaseMapper<SysCourse> {

    Page<SysCourse> findPage(Page<SysCourse> page, @Param("name") String name);

    void delStudentCourse(Integer courseId, Integer studentId);
    void insStudentCourse(Integer courseId, Integer studentId);
}
