package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Driver;
import com.example.demo.entity.User;
import com.example.demo.mapper.DriverMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;

@RestController//controller里面的方法都以json格式输出
@RequestMapping("/user")//处理/user
public class UserController {

    @Resource
    UserMapper userMapper;
    @Resource
    DriverMapper driverMapper;

    @PostMapping("/login")//处理/login
    public Result<?> login(@RequestBody User user)throws JsonProcessingException {
        String token= TokenUtil.sign(user);
        Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword());
        User person = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
        if(person == null) {
            return Result.error("1","用户名或密码错误");
        }
        person.setPassword(null);
        HashMap<String,Object> hs=new HashMap<>();
        hs.put("token",token);
        hs.put("user",person);
        return Result.success(hs);
    }

    @PostMapping("/self")//处理所有的需要验证用户的情况，id储存在sessionStorage里，sessionStorage不可信
    public Result<?> getMyself(@RequestBody String author) {
        String username =TokenUtil.getUsername(author);
        Wrappers.<User>lambdaQuery().eq(User::getUsername,username);
        User person = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,username));
        if(person == null) {
            return Result.error("202","用户信息错误");
        }
        person.setPassword(null);//不能暴露密码
        return Result.success(person);
    }

    @PostMapping("/relog")//处理所有的需要验证用户的情况，id储存在sessionStorage里，sessionStorage不可信
    public Result<?> relog(@RequestBody String author) {
        String username =TokenUtil.getUsername(author);
        Wrappers.<User>lambdaQuery().eq(User::getUsername,username);
        User person = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,username));
        if(person == null) {
            return Result.error("202","用户信息错误");
        }
        return Result.success(person);
    }

    @PostMapping("/who")//处理所有的需要验证用户的情况，id储存在sessionStorage里，sessionStorage不可信
    public Result<?> getUser(@RequestBody Integer id) {
        User person = userMapper.selectById(id);
        if(person == null) {
            return Result.error("101","该用户不存在！");
        }
        person.setPassword(null);//不能暴露密码
        return Result.success(person);
    }

    @PostMapping("/nametoid")//处理所有的需要验证用户的情况，id储存在sessionStorage里，sessionStorage不可信
    public Result<?> getUserByName(@RequestBody String name) {
        User person = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,name));
        if(person == null) {
            return Result.error("101","该用户不存在！");
        }
        person.setPassword(null);//不能暴露密码
        return Result.success(person);
    }

    @PostMapping("")
    public Result<?> save(@RequestBody User user){
        Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername());
        User person = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(person != null) {
            return Result.error("300","用户已存在");
        }
        userMapper.insert(user);
        if(user.getAuthority().equals("employee")){
            Driver employee = new Driver();
            employee.setStatus("空闲");
            employee.setDriverid(user.getId());
            employee.setPhonenum(user.getPhonenum());
            driverMapper.insert(employee);
        }
        return Result.success();
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername());
        User person = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(person != null) {
            return Result.error("300","用户已存在");
        }
        user.setAuthority("user");
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        Driver driver= driverMapper.selectOne(Wrappers.<Driver>lambdaQuery().eq(Driver::getDriverid,id));
        if(driver != null){
            driverMapper.deleteById(driver.getId());
        }
        userMapper.deleteById(id);
        return Result.success();
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum ,
                              @RequestParam(defaultValue = "10") Integer pageSize ,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(User::getUsername, search);
        }
        Page<User> a = userMapper.selectPage(new Page<>(pageNum , pageSize), wrapper);
        return Result.success(a);
    }
}
