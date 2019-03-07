package com.zsf.container.controller;


import com.zsf.container.entity.CommoditiesCode;
import com.zsf.container.entity.Commodity;
import com.zsf.container.entity.Drinks;
import com.zsf.container.service.CommoditiesCodeService;
import com.zsf.container.service.CommodityService;
import com.zsf.container.service.DrinksService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ReM
 */
@RestController
@RequestMapping("/api/drinks")
public class DrinksController extends BaseController {

    @Autowired
    private DrinksService drinksService;
    @Autowired
    private CommoditiesCodeService commoditiesCodeService;
    @Autowired
    private CommodityService commodityService;

    @RequestMapping
    public List<Drinks> getDrinks() {
        return drinksService.findAll();
    }

    @PostMapping("/add")
    public String saveDrinks(@RequestBody(required = false) Drinks drinks) {

        drinksService.save(drinks);
        return SUCCESS;
    }

    /**
     * 修改酒类信息
     * @param drinks
     * @return
     */
    @PutMapping("/update")
    public String updateDrinks(@RequestBody(required = false) Drinks drinks) {

        // 根据类型和是否在库查询酒类的RFID条码有多少，然后记录到酒类信息中
        Integer count = commoditiesCodeService.countByTypeAndIsInOrType(drinks.getCommoditiesType(),1);

        // 根据类型查询酒类的RFID条码有多少，然后记录到酒类信息中
        Integer totalNumber = commoditiesCodeService.countByTypeAndIsInOrType(drinks.getCommoditiesType(),null);

        // 同步修改大类中的商品名称和在库数量
        commodityService.saveSumAndName(count,totalNumber,drinks.getName(),drinks.getCommoditiesType());

        drinks.setSum(count);
        drinks.setTotalNumber(totalNumber);
        drinksService.save(drinks);
        return SUCCESS;
    }
}
