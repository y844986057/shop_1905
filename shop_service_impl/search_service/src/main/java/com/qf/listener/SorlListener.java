package com.qf.listener;


import com.qf.entity.Goods;
import com.qf.service.impl.ISearchService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SorlListener {

    @Autowired
    private ISearchService searchService;

    @RabbitListener(queues = "search_queue")
    public void goodsToSorl(Goods goods){
        searchService.add(goods);
    }
}
