package com.qf.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "t_order")
public class Order implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.id
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    @Id
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.uid
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    private Integer uid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.state
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    private Integer state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.create_time
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.total_price
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    private Double totalPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.username
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.phone
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order.address
     *
     * @mbggenerated Mon Sep 16 16:56:52 CST 2019
     */
    private String address;

    private List<OrderDetail> orderDetailList;
}