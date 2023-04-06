package com.aiye.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.aiye.springboot.common.Constants;
import com.aiye.springboot.controller.dto.UserDTO;
import com.aiye.springboot.entity.SysMenu;
import com.aiye.springboot.entity.User;
import com.aiye.springboot.exception.ServiceException;
import com.aiye.springboot.mapper.SysRoleMapper;
import com.aiye.springboot.mapper.SysRoleMenuMapper;
import com.aiye.springboot.mapper.UserMapper;
import com.aiye.springboot.service.ISysMenuService;
import com.aiye.springboot.service.IUserService;
import com.aiye.springboot.util.TokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aiye
 * @since 2022-12-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private  static final Log LOG = Log.get();

    @Resource
    private UserMapper userMapper;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private ISysMenuService iSysMenuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User user = getUserInfo(userDTO);
        if (user != null && user.getUsername() != null){
            BeanUtil.copyProperties(user,userDTO,true);//源对象，复制对象,ignoreCase
            //设置token
            String token = TokenUtils.getToken(user.getId().toString(),user.getPassword());
            userDTO.setToken(token);

            String role = user.getRole();
            //设置用户的菜单列表
            List<SysMenu> roleMenus= getRoleMenus(role);
            userDTO.setMenus(roleMenus);
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
        return userDTO;
    }

    @Override
    public User register(UserDTO userDTO) {
        User user = getUserInfo(userDTO);
        if (user == null){
            user = new User();
            BeanUtil.copyProperties(userDTO,user,true);
            save(user);
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return user;
    }

    @Override
    public Page<User> findPage(Page<User> page, String username, String email, String address) {
        return userMapper.findPage(page,username,email,address);
    }

    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User user ;
        try{
            user = getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return user;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<SysMenu> getRoleMenus(String roleFlag){
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        //当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出系统所有的菜单
        List<SysMenu> menus = iSysMenuService.findMenus("");
        List<SysMenu> roleMenus = new ArrayList<>();
        //筛选当前用户的菜单
        for(SysMenu menu : menus){
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<SysMenu> children = menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
