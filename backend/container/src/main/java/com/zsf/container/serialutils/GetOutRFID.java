package com.zsf.container.serialutils;

import com.zsf.container.entity.*;
import com.zsf.container.service.impl.*;
import com.zsf.smart.handler.TagManager;
import com.zsf.smart.util.SmartHelper;
import org.springframework.data.domain.Example;

import java.util.List;

public class GetOutRFID {

    private SmartHelper smartHelper;

    TagManager tagManager = new TagManager() {
        @Override
        public void inventory(List<String> list) {
            operateRFID(list);
        }

        @Override
        public void diffrent(List<String> list) {

        }
    };

    public List<CommoditiesCode> operateRFID(List<String> list) {

        // 获取CommoditiesCodeServiceImpl bean
        CommoditiesCodeServiceImpl commoditiesCodeService = SpringUtils.getBean(CommoditiesCodeServiceImpl.class);
//        // 查询数据库标签集合并验证
        List<CommoditiesCode> commoditiesCodeList = commoditiesCodeService.findAll();
        for (CommoditiesCode cm : commoditiesCodeList) {
            boolean bl = false;
            if (list.size() > 0) {
                for (String str : list) {
                    if (cm != null) {
                        if (cm.getCommoditiesCode().equals(str)) {
                            // 数据吻合，设置状态为1（未拿走）
//                            cm.setSearchCount(cm.getSearchCount() + 1);
                            bl = true;
                            break;
                        }
                    }
                }
            }
            if (bl) {
                if (cm.getIsIn() != 0) {
                    cm.setIsIn(0);
                } else {
                    bl = false;
                }
            } else {
                if (cm.getIsIn() != 1) {
                    cm.setIsIn(1);
                    bl = true;
                }
            }
            if (bl) {
                commoditiesCodeService.save(cm);
            }
            // 如果扫描次数大于3次则未拿出
            // cm.getReserve3() // 扫描次数
//                    if (cm.getSearchCount() > 0) {
//                        // 拿出商品是否显示状态
//                        cm.setIsIn(1);
//                        // 商品是否在库状态
//                        cm.setReserve1(1);
//                        // 拿出商品每次扫面不到统计次数
//                        cm.setReserve2(0);
//                        // 每次扫描到商品的统计次数
//                        cm.setSearchCount(0);
//                    } else if (cm.getReserve3() > 1 && cm.getSearchCount() <1){
//                        cm.setIsIn(0);
//                        cm.setReserve1(0);
//                        // 预留字段，现改为统计不在库次数，达到一定次数不跳转页面
//                        cm.setReserve2(cm.getReserve2() + 1);
//                    }
//                    // 如果拿出商品扫描六次还没放回去设置拿出商品显示状态为1（不显示）
//                    if (cm.getReserve2() > 1) {
//                        cm.setIsIn(1);
//                    }
//                    if (cm.getReserve3() > 3) {
//                        cm.setReserve3(0);
//                    }
//                }
//                cm.setReserve3(cm.getReserve3()+1);

        }
        return commoditiesCodeService.findAll();
    }

    public void openRFID() {

        try {
            smartHelper = new SmartHelper(tagManager);
            smartHelper.open("192.168.0.178", "4001");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void inventory() {
        smartHelper.inventory(800);
    }

    public void closeRFID() {
        smartHelper.close();
    }
}
