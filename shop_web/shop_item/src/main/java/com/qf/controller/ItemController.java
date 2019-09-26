package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.impl.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/item")
public class ItemController {

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private Configuration configuration;

    @RequestMapping(value = "/getGoodsById/{id}")
    public String getGoodsById(@PathVariable Integer id, Model model){
        Goods goods = goodsService.getById(id);
        model.addAttribute("goods",goods);
        return "goodsList";
    }

    @RequestMapping(value = "/createHtml")
    @ResponseBody
    public String createHtml(Integer gid){
        Goods goods = goodsService.getById(gid);
        Map<String, Object> map = new HashMap<>();
        map.put("goods",goods);
        map.put("picList",goods.getGpic().split("\\|"));
        map.put("contextPath","/");
        Writer writer=null;
        try {
            Template template = configuration.getTemplate("goodsList.ftl");
            String path = ItemController.class.getClassLoader().getResource("static").getPath();
            File file=new File(path+File.separator+"pages");
            if (!file.exists()){
                file.mkdirs();
            }
            writer=new FileWriter(file.getAbsolutePath()+File.separator+goods.getId()+".html");
            template.process(map,writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return "ok";
    }
}
