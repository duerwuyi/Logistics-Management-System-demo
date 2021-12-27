package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Car;
import com.example.demo.entity.Order;
import com.example.demo.mapper.CarMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Wrapper;

@RestController
@RequestMapping("/car")
public class CarController{

    @Resource
    CarMapper carMapper;

    @PostMapping
    public Result<?> save(@RequestBody Car car){
        carMapper.insert(car);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Car car){
        carMapper.updateById(car);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        carMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum ,
                              @RequestParam(defaultValue = "10") Integer pageSize ,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Car> wrapper = Wrappers.<Car>lambdaQuery();
        if(search.equals("找车")){
            wrapper.eq(Car::getStatus, "空闲");
        }
        else if(StrUtil.isNotBlank(search)){
            wrapper.like(Car::getCarname, search);
        }
        Page<Car> a = carMapper.selectPage(new Page<>(pageNum , pageSize), wrapper);
        return Result.success(a);
    }
}