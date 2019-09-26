package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.impl.IGoodsService;
import com.qf.service.impl.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @Reference
    private IGoodsService goodsService;

    @Reference
    private ISearchService searchServer;



    @RequestMapping(value = "/searchGoods")
    public String searchGoods(String keyWorld, Model model){
        List<Goods> list=searchServer.getKeyWorldList(keyWorld);
        model.addAttribute("list",list);
        System.out.println(list);
        System.out.println("keyWorld:"+keyWorld);
        return "searchList";
    }




}
