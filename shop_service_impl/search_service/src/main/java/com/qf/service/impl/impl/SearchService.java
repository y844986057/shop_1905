package com.qf.service.impl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.impl.ISearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService implements ISearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public List<Goods> getKeyWorldList(String keyWorld) {
        List<Goods> list=new ArrayList<Goods>();
        SolrQuery query = new SolrQuery();
        query.setFacetLimit(-1);
        query.setFacetMinCount(1);
        query.setStart(0);
        query.setRows(12);
        if (StringUtils.isEmpty(keyWorld)){
            query.setQuery("gname:*  || gdesc:*");
        }else {
            String query1="gname:%s || gdesc:%s";
            query.setHighlight(true);
            query.setQuery(String.format(query1,keyWorld,keyWorld));

        }

        try {
            query.setHighlightSimplePre("<font color='red'>");
            query.setHighlightSimplePost("</font>");
            query.addHighlightField("gname");
            QueryResponse queryResponse = solrClient.query(query);
            SolrDocumentList results = queryResponse.getResults();
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

            for (SolrDocument sd : results) {
                String id = sd.getFieldValue("id").toString();
                String gname = sd.getFieldValue("gname").toString();
                String gdesc = sd.getFieldValue("gdesc").toString();
                Object gpic1 = sd.getFieldValue("gpic");
                String gpic=null;
                if (gpic1!=null){
                     gpic = gpic1.toString();
                }

                String gprice = sd.getFieldValue("gprice").toString();
                Goods goods = new Goods();
                goods.setId(Integer.parseInt(id));
                goods.setGname(gname);
                goods.setGdesc(gdesc);
                goods.setGpic(gpic);
                goods.setGprice(Double.parseDouble(gprice));
                if (highlighting!=null){
                    Map<String, List<String>> stringListMap = highlighting.get(id);
                    if (stringListMap.containsKey("gname")){
                        String gname1 = stringListMap.get("gname").get(0);                  goods.setGname(gname1);
                    }
                }
                list.add(goods);
            }


        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public void add(Goods goods){
        SolrInputDocument document = new SolrInputDocument();
        document.addField("gname",goods.getGname());
        document.addField("gdesc",goods.getGdesc());
        document.addField("gprice",goods.getGprice());
        document.addField("gpic",goods.getGpic());
        document.addField("id",goods.getId());
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
