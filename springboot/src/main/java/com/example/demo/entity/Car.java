package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("car")
@Data
public class Car {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String carname;
    private Integer maxweight;
    private String status;

}
