package com.aiye.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aiye.springboot.common.Constants;
import com.aiye.springboot.common.Result;
import com.aiye.springboot.controller.dto.UserDTO;
import com.aiye.springboot.util.TokenUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.aiye.springboot.service.IUserService;
import com.aiye.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author aiye
 * @since 2022-12-12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return null;
        }
        Result rt = Result.success(userService.register(userDTO));
        return rt;
    }
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO userDTO = userService.login(user);
        Result rt = Result.success(userDTO);
        return rt;
    }

    /**
     * 保存，新增修改
     * @param user
     * @return
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    @DeleteMapping("/{id}")//删除
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }
/**/
    @PostMapping("/del/batch")//删除多个
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping//查询所有
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/role/{role}")//查询所有
    public Result findUsersByRole(@PathVariable String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role",role );
        List<User> list = userService.list(queryWrapper );
        return Result.success(list);
    }

    @GetMapping("/{id}")//id查询
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/page")//分页查询
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email,
                               @RequestParam(defaultValue = "") String address) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if(!"".equals(username)){
//            queryWrapper.like("username",username);
//        }
//        if(!"".equals(email)){
//            queryWrapper.like("email",email);
//        }
//        if(!"".equals(address)){
//            queryWrapper.like("address",address);
//        }
//        queryWrapper.orderByAsc("id");
//        User currentUser = TokenUtils.getCurrentUser();
//        if(currentUser!=null ){
//            System.out.println("获取当前用户信息："  +currentUser.getNickname());
//        }else{
//            System.out.println("无法获取当前用户信息");
//        }
        return Result.success(userService.findPage(new Page<>(pageNum, pageSize),username,email,address));
    }

    /**
     * 导出用户数据接口
     */
    @GetMapping("/exportUserData")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库中查询所有的数据
        List<User> list = userService.list();
        //通过工具类创建writer写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter("d:/writeBeanTest.xlsx");
        //在内存操作写出到浏览器上
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话");
//        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("createTime","创建时间");
//        writer.addHeaderAlias("avatarUrl","头像");
        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        //设置浏览器相应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
    // 关闭writer，释放内存
        writer.close();
    }

    /**
     * 导入用户数据
     */
    @PostMapping("/importUserData")
    public boolean impUser(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //表头必须英文且和user对应
//        List<User> list = reader.readAll(User.class);
        //忽略表头内容，读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row:list             ) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        System.out.println(list);
        return true;
    }

    @GetMapping("/username/{username}")//用户信息
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }

}

