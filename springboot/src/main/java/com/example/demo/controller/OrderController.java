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
import com.example.demo.mapper.CarMapper;
import com.example.demo.mapper.DriverMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.DriverManager;
import java.sql.Wrapper;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderMapper orderMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    CarMapper carMapper;
    @Resource
    DriverMapper driverMapper;

    @PostMapping
    public Result<?> save(@RequestBody Order order){
        User sender = userMapper.selectById(order.getSenderid());
        User reveiver = userMapper.selectById(order.getReceiverid());
        User employee = userMapper.selectById(order.getEmployeeid());
        Car car = carMapper.selectById(order.getCarid());
        if( sender ==null || reveiver== null || employee == null
                || car ==null ||order.getSendadd()==null||order.getReceadd()==null)
        {
            return Result.error("304","订单没有完整填写或无效用户！");
        }
        if(order.getSenderid().equals(order.getReceiverid())){
            return Result.error("306","收发者相同！");
        }
        order.setStatus("preparing");
        order.setCheckfinish("否");
        if(sender.getAuthority().equals("user")
                &&reveiver.getAuthority().equals("user")
                &&employee.getAuthority().equals("employee")){
            if(car.getMaxweight()<order.getWeight()){
                return Result.error("305","车辆配重过多！");
            }
            orderMapper.insert(order);

            car.setStatus("正在使用");
            carMapper.updateById(car);
            Driver driver = driverMapper.selectOne( Wrappers.<Driver>lambdaQuery().eq(Driver::getDriverid,employee.getId()));
            driver.setStatus("任务中");
            driverMapper.updateById(driver);
            return Result.success();
        }
        return Result.error("90","人员错误！");
    }

    @PutMapping
    public Result<?> update(@RequestBody Order order){
        User sender = userMapper.selectById(order.getSenderid());
        User reveiver = userMapper.selectById(order.getReceiverid());
        User employee = userMapper.selectById(order.getEmployeeid());
        Car car = carMapper.selectById(order.getCarid());
        String a =  order.getSendadd();
        String b =order.getReceadd();
        if( sender ==null || reveiver== null || employee == null
                || car ==null ||a ==null || b ==null )
        {
            return Result.error("304","订单没有完整填写或无效用户！");
        }
        if(order.getSenderid().equals(order.getReceiverid())){
            return Result.error("306","收发者相同！");
        }
        if(sender.getAuthority().equals("user")&&reveiver.getAuthority().equals("user")&&employee.getAuthority().equals("employee")){
            if(car.getMaxweight()<order.getWeight()){
                return Result.error("305","车辆配重过多！");
            }
            orderMapper.updateById(order);
            if(order.getStatus().equals("preparing")  ||order.getStatus().equals("sending") ){
                car.setStatus("正在使用");
            }else{
                car.setStatus("空闲");
            }
            carMapper.updateById(car);
            Driver driver = driverMapper.selectOne( Wrappers.<Driver>lambdaQuery().eq(Driver::getDriverid,employee.getId()));
            if(order.getStatus().equals("preparing")  ||order.getStatus().equals("sending") ){
                driver.setStatus("任务中");
            }else{
                driver.setStatus("空闲");
            }
            driverMapper.updateById(driver);

            return Result.success();
        }
        return Result.error("90","人员错误！");
    }

    @PostMapping("/u")
    public Result<?> userSave(@RequestBody Order order){
        User sender = userMapper.selectById(order.getSenderid());
        User reveiver = userMapper.selectById(order.getReceiverid());
        order.setStatus("preparing");
        order.setCheckfinish("否");
        if( sender ==null || reveiver== null ||order.getSendadd()==null||order.getReceadd()==null)
        {
            return Result.error("304","订单没有完整填写或无效用户！");
        }
        if(order.getSenderid().equals(order.getReceiverid())){
            return Result.error("306","收发者相同！");
        }
        if(sender.getAuthority().equals("user")&&reveiver.getAuthority().equals("user")){
            orderMapper.insert(order);
            return Result.success();
        }
        return Result.error("90","人员错误！");
    }

    @PutMapping("/u")
    public Result<?> userUpdate(@RequestBody Order order){
        User sender = userMapper.selectById(order.getSenderid());
        User reveiver = userMapper.selectById(order.getReceiverid());
        if( sender ==null || reveiver== null ||order.getSendadd()==null||order.getReceadd()==null)
        {
            return Result.error("304","订单没有完整填写或无效用户！");
        }
        if(order.getSenderid().equals(order.getReceiverid())){
            return Result.error("306","收发者相同！");
        }
        if(sender.getAuthority().equals("user")&&reveiver.getAuthority().equals("user")){
            orderMapper.updateById(order);
            return Result.success();
        }
        return Result.error("90","人员错误！");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        orderMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/deleteAll")
    public Result<?> deleteAll(@RequestBody List<Integer> ids){
        orderMapper.deleteBatchIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum ,
                              @RequestParam(defaultValue = "10") Integer pageSize ,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String Flag,
                              @RequestParam(defaultValue = "0") Integer userID){
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery();
        if(Objects.equals(Flag, "userx")) {
            if(StrUtil.isNotBlank(search)){
                wrapper.like(Order::getOrdername, search).and(i->i.eq(Order::getSenderid, userID).or().eq(Order::getReceiverid,userID));
            }
            else{
                wrapper.eq(Order::getSenderid, userID).or().eq(Order::getReceiverid,userID);
            }
        }else if(Objects.equals(Flag, "usery")){
            if(StrUtil.isNotBlank(search)){
                wrapper.like(Order::getOrdername, search).and(i->i.eq(Order::getSenderid, userID));
            }
            else{
                wrapper.eq(Order::getSenderid, userID);
            }
        }
        else if(Objects.equals(Flag, "userz")){
            if(StrUtil.isNotBlank(search)){
                wrapper.like(Order::getOrdername, search).and(i->i.eq(Order::getReceiverid, userID));
            }
            else{
                wrapper.eq(Order::getReceiverid, userID);
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
        else if(Objects.equals(Flag, "empty")) {
            wrapper.eq(Order::getCarid, null);
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