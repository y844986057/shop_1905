package com.qf.springboot1905.search_service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.impl.IGoodsService;
import com.qf.service.impl.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceApplicationTests {

    @Autowired
    private ISearchService searchService;


    @Reference
    private IGoodsService goodsService;

    @Autowired
    private SolrClient solrClient;

    @Test
    public void contextLoads() throws IOException, SolrServerException {
        List<Goods> list = goodsService.getList();

        for (Goods goods : list) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("gname", goods.getGname());
            document.addField("gdesc", goods.getGdesc());
            document.addField("gprice", goods.getGprice());
            document.addField("gpic", goods.getGpic());
            document.addField("id", goods.getId());
            solrClient.add(document); // update/add
        }
        solrClient.commit();


    }

    @Test
    public void test111() throws IOException, SolrServerException {
        solrClient.deleteById("94");
        //solrClient.deleteByQuery("*:*");
        solrClient.commit();
    }

    @Test
    public void test1223() {
        List<Goods> list = searchService.getKeyWorldList("月饼");
        for (Goods goods : list) {
            System.out.println(goods);
        }
    }

    @Test
    public void test122311() throws IOException, SolrServerException {
        List<Goods> list = new ArrayList<Goods>();
        SolrQuery query = new SolrQuery();
        String query1 = "gname:%s || gdesc:%s";
        String keyWorld = "月饼";
        query.setQuery(String.format(query1, keyWorld, keyWorld));


        QueryResponse queryResponse = solrClient.query(query);
        SolrDocumentList results = queryResponse.getResults();
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        for (SolrDocument sd : results) {
            String id = sd.getFieldValue("id").toString();
            String gname = sd.getFieldValue("gname").toString();
            String gdesc = sd.getFieldValue("gdesc").toString();
            String gpic = sd.getFieldValue("gpic").toString();
            String gprice = sd.getFieldValue("gprice").toString();
            Goods goods = new Goods();
            goods.setId(Integer.parseInt(id));
            goods.setGname(gname);
            goods.setGdesc(gdesc);
            goods.setGpic(gpic);
            goods.setGprice(Double.parseDouble(gprice));
            list.add(goods);
        }
        for (Goods goods : list) {
            System.out.println(goods);
        }

    }
}
