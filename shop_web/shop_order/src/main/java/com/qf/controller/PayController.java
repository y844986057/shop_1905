package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Order;
import com.qf.service.impl.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PayController {

    @Reference
    private IOrderService orderService;

    @RequestMapping(value = "/pay")
    public void pay(String oid,HttpServletResponse response) {

        // 1.根据订单Id查询订单的对象
        Order order = orderService.getOrderById(oid);
        System.out.println(order);

        // 1.获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016101400683824","MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCp7fuhP5yLx1FKlcTUjSRKRUcWIliuBIbiv5KzAcIJzb8i5rai9Q5Q3SAt1gb5bCW4a3rfx4Tc/RWdlF910MpFeTlyVfKJobeAOf+RWKVGo8wHchC+r/1BEN3PbYneYaidO53vJJ66hGMy+79oX5a1bJBexwgS48uOaozr3Ary6Eh2ECxsXdLk4rqkFUESBzYpSLLTgEZNtyGoJ7g219x8xPtYnBG1Wr6bAshQMcClIr7HcES9mYn914t+24yUHOLIOg6BTPMqM+sjbDuhoNiiH+rpRFt1Jw8TP2GOvlC+5FZoycrVkYqzCVsMqN9NrKHtgcL+TlBq45VnWjveD6fZAgMBAAECggEAfxO7o6Cgf191CWPrKFDMnMYmHQVeb/Russ80860S4KX+lCkY19CO/iX/VIfD+27G3p1x7DuWvnR9X7R/lumm+kFnlntzvz0yry+wUY+LOcJwZ/kDULbnm2rzlZWDDszVfZAB0b7+/NjYHl5pi7JAUDh51obdINM9fKlt7UxG29Nk/+rJW8/njUBZ+j4bJ+fEjyr63hTYorUombYli58ffailv8Wh7FAAhgxlwH1l6me1CqNhgI8CsFDRkDeKYjXyq+IENGMydzpEV8ZaurTbAMG/gDJ7sD68hGqyOAhee+bCP3fPlXO0st2HYaUM4kNwGPYE+TKKSiVdptoKxtqHKQKBgQDfQvaP+M6G76pjX6XffqEBxPn/SOywSSRINqckWzGcSUVyHogfxPXGymmo60SqWok0IbcZW69+XeS8aSzjJ9MuNqxwmrpgmSxTLA8u+WXLzfnNmTYhlVp46AsV2ovmFRRvSrSqjXUKMHLdCbBQpVzEd5TiYQxODZEmA3kEL32yjwKBgQDC2PyVSCAn8Qq2jMdw1qwPb7pt1Zt1jSgjcb0W75Fspf6yuuFc75nQc2r63g/PbnSQCT+L6WLjVbc7LuqE5ESqVN/SlP8Jpd4HIYX3lI+5LCDZEv5rq6gfhJTHjCauoQ0Zgw0OPmudfJcWHzzjJ7hU6Ga4JbToBkKBNhpv1DwTFwKBgQCBeR3vxERQ8HAJjRBumcrZcdfg+fN5EBGgZ0Fqzg/pKHzDf31Fnz1A+WVo9nctq69hsiOy6v7M87qQoRXM7e2EIWTDMJfhKTMRUL3FahBkrqCtYKqJs0lyUG8NT15OIe9l0xcOyVYUgKA8G0BKpHs13HtVBR7TrP594wmtKbVldQKBgCP3K4BepNjajFCv4ax5Y58hz3awpBZprMwGFZIjPGOirEXPawPdkjXA6DFQRfjS+Z8QpfMsbD1sN/srqMxI2kfajMnxCLZ+MGJ+lJzjhjf25kVLVT0InWrb7R58RzOatLFN2F4VtYWHVtgVggan8UVy9GrvQtpFNYdmNjEIKuKXAoGAM5kv9nBtgi2YAWnQGIEJrB3u9Z22v/ie+hwyWjwIGc+6O6oNqb7e19hwufsmN3gg6jA8j3SqI0s3OLedmv/U3ulYJ6c3Go4mkdXa4exB9MPLVxc7/jXZL3wPZddWAK9BGzJuoRpEjFEGkClzprlnfYoUhYTHqlFr//b09sswVik=",
                "JSON",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs344rELqcHyusVTbdPCjc1LJIpBzr6EMZbKJf+hBsHjJo+VzS7DBl85CZSoVVFAekNsX4qSLB2VV2C4Qp9ZGV/izw2WUufU8ppJxZCKjxPRO7BiXSMFgyhiGGfQ/JQeENki40Vu0YOlk0seyKHFDhTFDQcDqvTTEzLvPORE0hoYNOx0SBHZyWxdgcPHU/XEz75nZJnv4kO476YNmXWehsKPulfB1KngY5sV9RtMOf63WJVu9I2e4RuCbk6RJE3weRWzF5xugHceXY1C4KnAy6ERUmhcjpte4hNyhvTYuv8+50JnVkdeWa8XTYI5RIB6t5ekVeCL1Q71rr5H/ZkkGLwIDAQAB",
                "RSA2");

        // 2.创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        alipayRequest.setReturnUrl("http://localhost:8081/"); // 同步地址

        alipayRequest.setNotifyUrl("http://kp9wxi.natappfree.cc/updateOrderState");//在公共参数中设置回跳和通知地址

        // //填充业务参数
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+order.getId()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+order.getTotalPrice()+"," +
                "    \"subject\":\""+order.getOrderDetailList().get(0).getGname()+"\"," +
                "    \"body\":\""+order.getOrderDetailList().get(0).getGdesc()+"\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");

        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        try {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value = "/updateOrderState",method = RequestMethod.POST)
    public void updateOrderState(String out_trade_no,String trade_status){
        if("TRADE_SUCCESS".equals(trade_status)){
            orderService.updateOrderState(out_trade_no,1);
        }



        System.out.println("PayController.updateOrderState");
    }


}

