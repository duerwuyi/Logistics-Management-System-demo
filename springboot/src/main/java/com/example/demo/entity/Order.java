package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("orderlist")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String ordername;
    private Integer senderid;
    private Integer receiverid;
    private Integer employeeid;
    private Integer carid;
    private String sendadd;
    private String receadd;
    private String status;
    private String checkfinish;
    private Integer weight;

    private Integer cost;
}
