package com.zsf.container.controller;
import com.zsf.container.result.Result;
import com.zsf.container.service.GoodsImgService;
import com.zsf.core.config.web.BusinessException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class GoodsImgController {



    @Autowired
    public GoodsImgService goodsImgService;

    @RequestMapping("/hello")
    public String hello(){
        return "Hello";
    }

    @PostMapping("/saveImg")
    public Result<Object> saveImg(MultipartFile[] files,@RequestParam(value = "commoditiesType") Integer commoditiesType, HttpServletRequest request) throws IOException, BusinessException {
        return goodsImgService.saveImg(files,commoditiesType);
    }
    @GetMapping("/getImg")
    public Result<Object> getImg(HttpServletRequest request) throws IOException {
        return goodsImgService.getImg(request);
    }

    @PostMapping("/deleteImg")
    public Result<Object> deleteImg(HttpServletRequest request) throws IOException {
        return goodsImgService.deleteImg(request);
    }

    @DeleteMapping("/deleteImgBySrc")
    public Integer deleteImgBySrc(@RequestParam(value = "src") String src,
                                  @RequestParam(value = "id") Long id) {
        goodsImgService.deleteImgBySrc(src,id);
        return 200;
    }

}
