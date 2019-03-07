package com.zsf.container.controller;

import com.zsf.container.entity.*;
import com.zsf.container.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ReM
 * @date 2019年1月26日 08:33:26
 */
@RestController
@RequestMapping("/api/getCommodity")
public class GetCommodityController {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private DrinksService drinksService;
    @Autowired
    private SmokeService smokeService;
    @Autowired
    private HealthProductsService healthProductsService;
    @Autowired
    private GoodsImgService goodsImgService;
    @RequestMapping
    public List<Object> getCommodity(@RequestBody(required = false) Integer[] commoditiesTypes) {

        List<Object> list = new ArrayList<>();
        Commodity commodity = new Commodity();
        Drinks drinks = new Drinks();
        Smoke smoke = new Smoke();
        HealthProducts healthProducts = new HealthProducts();
        GoodsImg goodsImg = new GoodsImg();
        for (Integer integer : commoditiesTypes) {
            switch (integer) {
                case 1 : {
                    commodity = getComm(integer);
                    drinks.setCommoditiesType(integer);
                    Example<Drinks> example = Example.of(drinks);
                    drinks = drinksService.findOne(example);
                    goodsImg.setCommoditiesType(integer);
                    Example<GoodsImg> exampleImg = Example.of(goodsImg);
                    drinks.setImg(goodsImgService.findAll(exampleImg));
                    commodity.setObject(drinks);
                    list.add(commodity);
                    break;
                }
                case 2 : {
                    commodity = getComm(integer);
                    smoke.setCommoditiesType(integer);
                    Example<Smoke> example = Example.of(smoke);
                    smoke = smokeService.findOne(example);
                    goodsImg.setCommoditiesType(integer);
                    Example<GoodsImg> exampleImg = Example.of(goodsImg);
                    smoke.setImg(goodsImgService.findAll(exampleImg));
                    commodity.setObject(smoke);
                    list.add(commodity);
                    break;
                }
                case 3 : {
                    commodity = getComm(integer);
                    healthProducts.setCommoditiesType(integer);
                    Example<HealthProducts> example = Example.of(healthProducts);
                    healthProducts = healthProductsService.findOne(example);
                    goodsImg.setCommoditiesType(integer);
                    Example<GoodsImg> exampleImg = Example.of(goodsImg);
                    healthProducts.setImg(goodsImgService.findAll(exampleImg));
                    commodity.setObject(healthProducts);
                    list.add(commodity);
                    break;
                }
            }
        }
        return list;
    }

    private Commodity getComm(Integer comType) {
        Commodity commodity = new Commodity();
        commodity.setCommoditiesType(comType);
        Example<Commodity> commodityExample = Example.of(commodity);
        commodity = commodityService.findOne(commodityExample);
        return commodity;
    }
}
