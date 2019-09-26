package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.entity.Goods;
import com.qf.entity.Page;
import com.qf.entity.ResultEntity;
import com.qf.service.impl.IGoodsService;
import com.qf.service.impl.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping(value = "/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;
    @Reference
    private ISearchService searchService;

    @Value("${fastdf.path}")
    private String fastDFSPath; // FastDFS文件服务器的路径

    @Autowired
    private FastFileStorageClient client;

    @RequestMapping(value = "/getGoodsPage")
    public String  getGoodsPage(Page page, Model model){
        PageInfo<Goods> pageInfo = goodsService.getPage(page.getPageNum(), page.getPageSize());
        model.addAttribute("page",pageInfo);
        model.addAttribute("url","goods/getGoodsPage");
        return "goods/goodsList";
    }

    @RequestMapping(value = "/uploadFile")
    @ResponseBody
    public String uploadFile(MultipartFile file, HttpServletRequest req)  {
        String fileName = file.getOriginalFilename();
        String fileExtName = ""; // 文件类型
        String filePath = "";
        // xxxxx.png
        fileExtName = fileName.substring(fileName.lastIndexOf(".")+1);
        try {
            StorePath storePath = client.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), fileExtName, null);
            filePath = storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fastDFSPath+filePath;
    }

    @RequestMapping(value = "/addGoods")
    @ResponseBody
    public ResultEntity<String> addGoods(Goods goods){

        int id = goodsService.add(goods);
        return ResultEntity.successMessage();
    }
}
