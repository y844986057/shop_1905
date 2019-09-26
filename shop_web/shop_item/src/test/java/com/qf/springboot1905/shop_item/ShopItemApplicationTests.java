package com.qf.springboot1905.shop_item;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.controller.ItemController;
import com.qf.entity.Goods;
import com.qf.service.impl.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopItemApplicationTests {
    @Autowired
    private Configuration configuration;

    @Reference
    private IGoodsService goodsService;
    @Test
    public void contextLoads() {
    }
    @Test
    public void testUpdateItemTemplate(){
        List<Goods> goodsList = goodsService.getList();
        for (Goods g:goodsList) {
            updateTemplate(g.getId());
        }
    }

    public void updateTemplate(Integer goodsId){
        // 1.根据商品Id查询对象
        Goods goods = goodsService.getById(goodsId);

        Map<String,Object> map = new HashMap<String ,Object>();
        map.put("contextPath","/");
        map.put("picList",goods.getGpic().split("\\|")); // ?
        map.put("goods",goods);


        // 2.获取模板
        Template template = null;
        Writer writer =null;
        try {
            template = configuration.getTemplate("goodsItem.ftl");

            String path = ItemController.class.getClassLoader().getResource("static").getPath();

            File file = new File(path+ File.separator+"pages");

            // 如果没有就创建
            if(!file.exists()){
                file.mkdirs();
            }

//            /xxxxx/static/page/1.html
            // 3.设置生成页面的路径
            writer = new FileWriter( file.getAbsolutePath()+File.separator+goods.getId()+".html");

            // 4.根据模板和数据生成页面
            template.process(map,writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
