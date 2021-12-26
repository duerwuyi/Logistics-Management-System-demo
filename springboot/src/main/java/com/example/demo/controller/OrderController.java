package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderMapper orderMapper;

    @PostMapping
    public Result<?> save(@RequestBody Order order){
        orderMapper.insert(order);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Order order){
        orderMapper.updateById(order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        orderMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum ,
                              @RequestParam(defaultValue = "10") Integer pageSize ,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String Flag,
                              @RequestParam(defaultValue = "0") Integer userID){
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery();
        if(Objects.equals(Flag, "user")) {
            if(StrUtil.isNotBlank(search)){
                wrapper.like(Order::getOrdername, search).and(i->i.eq(Order::getSenderid, userID).or().eq(Order::getReceiverid,userID));
            }
            else{
                wrapper.eq(Order::getSenderid, userID).or().eq(Order::getReceiverid,userID);
            }
        }
        else if(Objects.equals(Flag, "driver")) {
            if(StrUtil.isNotBlank(search)){
                wrapper.like(Order::getOrdername, search).eq(Order::getEmployeeid, userID);
            }
            else{
                wrapper.eq(Order::getEmployeeid, userID);
            }
        }
        else{
            if(StrUtil.isNotBlank(search)){
                wrapper.like(Order::getOrdername, search);
            }
        }
        Page<Order> a = orderMapper.selectPage(new Page<>(pageNum , pageSize), wrapper);
        return Result.success(a);

    }
}