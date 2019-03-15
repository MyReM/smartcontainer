package com.zsf.container.controller;

import com.zsf.container.entity.CommoditiesCode;
import com.zsf.container.serialutils.GetOutRFID;
import com.zsf.container.service.CommoditiesCodeService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/rfid")
public class RfidController extends BaseController {

    @Autowired
    CommoditiesCodeService commoditiesCodeService;

    private GetOutRFID getOutRFID = new GetOutRFID();

    @GetMapping("/getRFID")
    public String getRFID(){
        getOutRFID.inventory();
        return SUCCESS;
    }

    @GetMapping("/getNonIn")
    public List<Object> getNonIn(){

        CommoditiesCode commoditiesCode = new CommoditiesCode();
        commoditiesCode.setIsIn(0);
        Example<CommoditiesCode> example = Example.of(commoditiesCode);
        List<CommoditiesCode> lis = commoditiesCodeService.findAll(example);

        List<Integer> ins = new ArrayList<>();
        List<Integer> productNum = new ArrayList<>();
        int dnum = 0, snum = 0, hnum = 0;
        for (CommoditiesCode cmc : lis) {
            ins.add(cmc.getCommoditiesType());
            switch(cmc.getCommoditiesType()) {
                case 1 : {
                    dnum += 1;
                    break;
                }
                case 2 : {
                    snum += 1;
                    break;
                }
                case 3 : {
                    hnum += 1;
                    break;
                }
            }
        }
        productNum.add(dnum);
        productNum.add(snum);
        productNum.add(hnum);

        Set<Integer> set = new HashSet<>(ins);
        List<Integer> list = new ArrayList<>(set);



        List<Object> objects = new ArrayList<>();
        objects.add(list);
        objects.add(productNum);
        return objects;
    }

    @GetMapping("/openRFID")
    public void openRFID(){
        getOutRFID.openRFID();
        getOutRFID.inventory();
    }

    @GetMapping("/close")
    public void closeRFID(){
        getOutRFID.closeRFID();
    }
}
