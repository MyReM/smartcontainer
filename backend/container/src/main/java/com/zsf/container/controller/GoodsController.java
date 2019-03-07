package com.zsf.container.controller;

import com.zsf.container.service.GoodsService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/container/goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

}
