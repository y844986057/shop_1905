package com.qf.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.controller.ItemController;
import com.qf.entity.Goods;
import com.qf.service.impl.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
public class ItemListener {

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private Configuration configuration;

    @RabbitListener(queues = "item_queue")
    public void addHtml(Goods goods1){
        Goods goods = goodsService.getById(goods1.getId());
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

    }
}
