package com.aiye.springboot.service;

import com.aiye.springboot.controller.dto.UserDTO;
import com.aiye.springboot.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aiye
 * @since 2022-12-12
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO user);

    User register(UserDTO user);

    Page<User> findPage(Page<User> tPage, String username, String email, String address);
}
