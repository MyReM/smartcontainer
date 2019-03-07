package com.zsf.container.controller;

import com.zsf.container.entity.CommoditiesCode;
import com.zsf.container.entity.Smoke;
import com.zsf.container.service.CommoditiesCodeService;
import com.zsf.container.service.CommodityService;
import com.zsf.container.service.SmokeService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ReM
 */
@RestController
@RequestMapping("/api/smoke")
public class SmokeController extends BaseController {

    @Autowired
    private SmokeService smokeService;
    @Autowired
    private CommoditiesCodeService commoditiesCodeService;
    @Autowired
    private CommodityService commodityService;

    @RequestMapping
    public List<Smoke> getDrinks() {
        return smokeService.findAll();
    }

    @PostMapping("/add")
    public String saveSmoke(@RequestBody(required = false) Smoke smoke) {

        smokeService.save(smoke);
        return SUCCESS;
    }

    /**
     * 修改烟类信息
     * @param smoke
     * @return
     */
    @PutMapping("/update")
    public String updateSmoke(@RequestBody(required = false) Smoke smoke) {

        // 根据类型和是否在库查询酒类的RFID条码有多少，然后记录到酒类信息中
        Integer count = commoditiesCodeService.countByTypeAndIsInOrType(smoke.getCommoditiesType(),1);

        // 根据类型查询酒类的RFID条码有多少，然后记录到酒类信息中
        Integer totalNumber = commoditiesCodeService.countByTypeAndIsInOrType(smoke.getCommoditiesType(),null);

        // 同步修改大类中的商品名称和在库数量
        commodityService.saveSumAndName(count,totalNumber,smoke.getName(),smoke.getCommoditiesType());

        smoke.setSum(count);
        smoke.setTotalNumber(totalNumber);
        smokeService.save(smoke);
        return SUCCESS;
    }
}
