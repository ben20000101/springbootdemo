package com.aiye.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.aiye.springboot.common.Constants;
import com.aiye.springboot.common.Result;
import com.aiye.springboot.entity.Files;
import com.aiye.springboot.entity.User;
import com.aiye.springboot.mapper.FileMapper;
import com.aiye.springboot.util.TokenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        //定义一个文件唯一的标识
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        //先存储到磁盘
        File uploadFile = new File(fileUploadPath + fileUUID);
        //判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        //当文件存在的时候再获取文件的md5
        String md5 ;
        String url ;
        //把获取到的文件存储到磁盘目录
        file.transferTo(uploadFile);
        //获取文件md5，
        md5 = SecureUtil.md5(uploadFile);
        Files dbFiles = getFileMd5(md5);
        if (dbFiles!=null){
            url = dbFiles.getUrl();
            //文件存在则删除
            uploadFile.delete();
        }else{
            url = "http://localhost:8999/file/" +fileUUID;
        }
        //存储到数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        //性能高！从redis取数据，塞入一个，再设置（不用查询数据库）
        String json = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
        List<Files> files = JSONUtil.toBean(json, new TypeReference<List<Files>>() {
        },true);
        files.add(saveFile);
        setRedis(Constants.FILES_KEY, JSONUtil.toJsonStr(files));
        //从数据库查数据,方法二
//        List<Files> files = fileMapper.selectList(null);
//        setRedis(Constants.FILES_KEY,JSONUtil.toJsonStr(files));
        //直接删掉redis里的缓存的key，方法三
//        delRedisKey(Constants.FILES_KEY);

        return url;
    }

    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName , HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath+fileName);
        //设置输出流的格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节流
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();
    }

    private Files getFileMd5(String md5){
        //查询是否存在同一文件
        QueryWrapper<Files> queryWrapper = new QueryWrapper<Files>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
       return filesList.size() >0 ? filesList.get(0) : null;
    }

    @GetMapping("/page")//分页查询
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.eq("is_delete",false);
        User currentUser = TokenUtils.getCurrentUser();
        if(currentUser!=null ){
            System.out.println("获取当前用户信息："  +currentUser.getNickname());
        }else{
            System.out.println("无法获取当前用户信息");
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize),queryWrapper));
    }

    /**
     * 保存，新增修改
     * @param
     * @return
     */
//    @CachePut(value="files",key = "'frontAll'")
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        fileMapper.updateById(files);
        delRedisKey(Constants.FILES_KEY);
        return Result.success(fileMapper.selectList(null));
    }

    //清除一条缓存
//    @CacheEvict(value="files",key="'frontAll'")
    @DeleteMapping("/{id}")//删除
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setDelete(true);
        delRedisKey(Constants.FILES_KEY);
        return Result.success(fileMapper.updateById(files));
    }
    /**/
    @PostMapping("/del/batch")//删除多个
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files f : files) {
            f.setDelete(true);
             fileMapper.updateById(f) ;
        }
        delRedisKey(Constants.FILES_KEY);
        return Result.success();
    }

    /**
     * 删除缓存key
     */
    private void delRedisKey(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 设置缓存key和value
     * @param key
     * @param value
     */
    private void setRedis(String key ,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }

}
