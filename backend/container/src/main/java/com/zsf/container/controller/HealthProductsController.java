package com.zsf.container.controller;

import com.zsf.container.entity.CommoditiesCode;
import com.zsf.container.entity.Commodity;
import com.zsf.container.entity.HealthProducts;
import com.zsf.container.service.CommoditiesCodeService;
import com.zsf.container.service.CommodityService;
import com.zsf.container.service.HealthProductsService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ReM
 */
@RestController
@RequestMapping("/api/healthProducts")
public class HealthProductsController extends BaseController {

    @Autowired
    private HealthProductsService healthProductsService;
    @Autowired
    private CommoditiesCodeService commoditiesCodeService;
    @Autowired
    private CommodityService commodityService;

    @RequestMapping
    public List<HealthProducts> getDrinks() {
        return healthProductsService.findAll();
    }

    @PostMapping("/add")
    public String saveSmoke(@RequestBody(required = false) HealthProducts healthProducts) {

        healthProductsService.save(healthProducts);
        return SUCCESS;
    }

    /**
     * 修改保健品类信息
     * @param healthProducts
     * @return
     */
    @PutMapping("/update")
    public String updateSmoke(@RequestBody(required = false) HealthProducts healthProducts) {

        // 根据类型和是否在库查询酒类的RFID条码有多少，然后记录到酒类信息中
        Integer count = commoditiesCodeService.countByTypeAndIsInOrType(healthProducts.getCommoditiesType(),1);

        // 根据类型查询酒类的RFID条码有多少，然后记录到酒类信息中
        Integer totalNumber = commoditiesCodeService.countByTypeAndIsInOrType(healthProducts.getCommoditiesType(),null);

        // 同步修改大类中的商品名称和在库数量
        commodityService.saveSumAndName(count,totalNumber,healthProducts.getName(),healthProducts.getCommoditiesType());

        healthProducts.setSum(count);
        healthProducts.setTotalNumber(totalNumber);
        healthProductsService.save(healthProducts);
        return SUCCESS;
    }
}
