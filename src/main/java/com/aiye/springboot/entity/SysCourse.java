package com.aiye.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author aiye
 * @since 2022-12-27
 */
@Getter
@Setter
  @TableName("sys_course")
@ApiModel(value = "SysCourse对象", description = "")
public class SysCourse implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("ID")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("课程名称")
      private String name;

      @ApiModelProperty("学分")
      private Integer score;

      @ApiModelProperty("上课时间")
      private String times;

      @ApiModelProperty("是否开课")
      private Boolean state;

      @ApiModelProperty("授课老师id")
      private Integer teacherId;

      @TableField(exist = false)
      private String teacher;


}
