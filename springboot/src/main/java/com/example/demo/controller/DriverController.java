package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Car;
import com.example.demo.entity.Driver;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.mapper.DriverMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/driver")
public class DriverController {
    @Resource
    DriverMapper driverMapper;
    @Resource
    UserMapper userMapper;

    @PostMapping
    public Result<?> save(@RequestBody Driver driver){
        driverMapper.insert(driver);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Driver driver){
        driverMapper.updateById(driver);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        driverMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum ,
                              @RequestParam(defaultValue = "10") Integer pageSize ,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Driver> wrapper = Wrappers.<Driver>lambdaQuery();
        if(search.equals("找司机")){
            wrapper.eq(Driver::getStatus, "空闲");
        }
        else if(StrUtil.isNotBlank(search)){
            wrapper.like(Driver::getDriverid, search);
        }
        Page<Driver> a = driverMapper.selectPage(new Page<>(pageNum , pageSize), wrapper);
        return Result.success(a);

    }
}
