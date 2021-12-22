package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.Car;
import com.example.demo.mapper.CarMapper;

import javax.annotation.Resource;

@RestController
@RequestMapping("/car")
public class CarController{

    @Resource
    CarMapper carMapper;

}
