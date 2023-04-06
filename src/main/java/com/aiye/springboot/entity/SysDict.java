package com.aiye.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_dict")
public class SysDict {

    private Integer id;
    private String name;
    private String value ;
    private String type;

}
