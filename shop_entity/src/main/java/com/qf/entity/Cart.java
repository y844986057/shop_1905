package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_cart")
public class Cart implements  Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer gid; // 商品id

    private Integer num; // 商品购买数量

    private Integer uid; // 用户id

    private Double subTotal; // 小计

    private Date createTime; // 添加时间

    @Transient
    private Goods goods; // 商品信息
}
