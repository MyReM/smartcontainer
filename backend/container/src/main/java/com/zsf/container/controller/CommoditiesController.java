package com.zsf.container.controller;

import com.zsf.container.entity.CommoditiesCode;
import com.zsf.container.service.CommoditiesCodeService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author ReM
 */
@RestController
@RequestMapping("/api/commoditiesCode")
public class CommoditiesController extends BaseController {

    @Autowired
    private CommoditiesCodeService commoditiesCodeService;


    @RequestMapping("/getPage")
    public Page<CommoditiesCode> getPage(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size,
                                         CommoditiesCode commoditiesCode) {
        Page<CommoditiesCode> pages = commoditiesCodeService.getPage(page,size,commoditiesCode);
        return pages;
    }

    @PostMapping("/add")
    public String saveCommodities(@RequestBody(required = false) CommoditiesCode commoditiesCode) {

        // 判断条码是否存在
        CommoditiesCode commoditiesCode1 = new CommoditiesCode();
        commoditiesCode1.setCommoditiesCode(commoditiesCode.getCommoditiesCode());
        Example<CommoditiesCode> example = Example.of(commoditiesCode1);
        commoditiesCode1 = commoditiesCodeService.findOne(example);
        boolean bl = commoditiesCode1 == null ? true : false;
        if(bl) {
            commoditiesCode.setIsIn(1);
            commoditiesCodeService.save(commoditiesCode);
            return SUCCESS;
        } else {
            // 返回105表示已存在该条码
            return "105";
        }
    }

    @PutMapping("/update")
    public String updateCommoditiesCode(@RequestBody(required = false) CommoditiesCode commoditiesCode) {

        // 判断是否修改条码以及修改的条码是否重复
        CommoditiesCode commoditiesCode1 = commoditiesCodeService.findOne(commoditiesCode.getId());
        // 条码没有变化不做update操作
        if (commoditiesCode.getCommoditiesCode().equals(commoditiesCode1.getCommoditiesCode())) {
            return SUCCESS;
        } else {
            // 判断条码是否重复
            CommoditiesCode commoditiesCode2 = new CommoditiesCode();
            commoditiesCode2.setCommoditiesCode(commoditiesCode.getCommoditiesCode());
            Example<CommoditiesCode> example = Example.of(commoditiesCode2);
            commoditiesCode2 = commoditiesCodeService.findOne(example);
            boolean bl =  commoditiesCode2 == null ? true : false;
            if (bl) {
                // 非重复修改
                commoditiesCodeService.save(commoditiesCode);
                return SUCCESS;
            } else {
                // 重复返回105
                return "105";
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCommoditiesCode(@PathVariable(value = "id") Long id) {

        commoditiesCodeService.deleteById(id);
        return SUCCESS;
    }
}
