package com.zsf.container.service.impl;

import com.zsf.container.dao.GoodsDAO;
import com.zsf.container.entity.Goods;
import com.zsf.container.service.GoodsService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Long> implements GoodsService {

    private GoodsDAO goodsDAO;

    @Autowired
    public void setGoodsDAO(GoodsDAO goodsDAO){
        this.goodsDAO = goodsDAO;
        super.setBaseDAO(goodsDAO);
    }
}
