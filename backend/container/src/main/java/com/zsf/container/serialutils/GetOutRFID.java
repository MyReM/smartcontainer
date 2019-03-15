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

    private void operateRFID(List<String> list) {

        // 获取CommoditiesCodeServiceImpl bean
        CommoditiesCodeServiceImpl commoditiesCodeService = SpringUtils.getBean(CommoditiesCodeServiceImpl.class);
//        // 查询数据库标签集合并验证
        List<CommoditiesCode> commoditiesCodeList = commoditiesCodeService.findAll();
        // 对比数据库，若扫描到则为已拿出，修改状态
        for (CommoditiesCode cm : commoditiesCodeList) {
            boolean bl = false;
            if (list.size() > 0) {
                for (String str : list) {
                    if (cm != null) {
                        if (cm.getCommoditiesCode().equals(str)) {
                            bl = true;
                            break;
                        }
                    }
                }
            }
            // bl=true为扫描有结果（拿出），为false则扫描不到该条码（放回）
            // 并且根据bl判断是否执行数据库操作
            if (bl) {
                // 状态不等于0，修改状态，等于0不作处理并把bl设置为fale
                if (cm.getIsIn() != 0) {
                    cm.setIsIn(0);
                } else {
                    bl = false;
                }
            } else {
                // 状态不等于1，修改状态，等于1不作处理并把bl设置为true
                if (cm.getIsIn() != 1) {
                    cm.setIsIn(1);
                    bl = true;
                }
            }
            // 结有变，需要执行数据库操作
            if (bl) {
                commoditiesCodeService.save(cm);
            }
        }
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
