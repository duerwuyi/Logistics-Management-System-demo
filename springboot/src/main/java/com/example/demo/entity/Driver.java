package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("driver")
@Data
public class Driver {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer driverid;
    private String phonenum;
    private String status;

}