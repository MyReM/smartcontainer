package com.zsf.container.service.impl;

import com.zsf.container.dao.ImgPathDao;
import com.zsf.container.entity.ImgPath;
import com.zsf.container.service.ImgPathService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ReM
 */
@Service
public class ImgPathServiceImpl extends BaseServiceImpl<ImgPath,Long> implements ImgPathService {

    private ImgPathDao imgPathDao;

    @Autowired
    public void setImgPathDao(ImgPathDao imgPathDao) {
        this.imgPathDao = imgPathDao;
        super.setBaseDAO(imgPathDao);
    }
}
