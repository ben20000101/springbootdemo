package com.aiye.springboot.controller.dto;

import com.aiye.springboot.entity.SysMenu;
import lombok.Data;

import java.util.List;


/**
 * 接收登录的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;

    private String role;
    private List<SysMenu> menus;
}
