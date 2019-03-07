package com.zsf.container.controller;

import com.zsf.container.entity.Commodity;
import com.zsf.container.entity.Drinks;
import com.zsf.container.entity.GoodsImg;
import com.zsf.container.entity.HealthProducts;
import com.zsf.container.service.*;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ReM
 */
@RestController
@RequestMapping("/api/commodity")
public class CommodityController extends BaseController {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private GoodsImgService goodsImgService;
    @Autowired
    private DrinksService drinksService;
    @Autowired
    private SmokeService smokeService;
    @Autowired
    private HealthProductsService healthProductsService;
    @Autowired
    private CommoditiesCodeService commoditiesCodeService;

    /**
     * 查询所有商品种类
     * @param commodity
     * @return
     */
    @RequestMapping
    public List<Commodity> getAllCommodity(Commodity commodity){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Commodity> example = Example.of(commodity,exampleMatcher);
        List<Commodity> list = commodityService.findAll(example);
        // 查询图片
        GoodsImg goodsImg = new GoodsImg();
        for (Commodity commodity1 : list) {
            goodsImg.setCommoditiesType(commodity1.getCommoditiesType());
            Example<GoodsImg> example1 = Example.of(goodsImg);
            List<GoodsImg> imgs = goodsImgService.findAll(example1);
            commodity1.setGoodsImg(imgs.get(0));
        }
        return list;
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @param commodity
     * @return
     */
    @RequestMapping("/getPage")
    public Page<Commodity> getPage(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                   @RequestParam(value = "size",defaultValue = "10") Integer size,
                                   Commodity commodity) {
        Page<Commodity> pages = commodityService.getPage(page,size,commodity);
        return pages;
    }

    /**
     * 修改商品
     * @param commodity
     * @return
     */
    @PutMapping("/update")
    public String updateCommodity(@RequestBody(required = false) Commodity commodity) {
        commodityService.save(commodity);
        return SUCCESS;
    }

    /**
     * 添加商品
     * @param commodity
     * @return
     */
    @PostMapping("/add")
    public String addCommodity(@RequestBody Commodity commodity) {
        commodityService.save(commodity);
        return SUCCESS;
    }
    /**
     * 删除商品
     * @param id
     */
    @DeleteMapping("/delete/{id}")
    public void deleteCommodity(@PathVariable(value = "id") Long id) {
        commodityService.deleteById(id);
    }
}
