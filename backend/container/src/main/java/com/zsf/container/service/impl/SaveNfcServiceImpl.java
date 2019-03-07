package com.zsf.container.service.impl;

import com.zsf.container.dao.SaveNFCDao;
import com.zsf.container.entity.SaveNfc;
import com.zsf.container.service.SaveNfcService;
import com.zsf.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveNfcServiceImpl extends BaseServiceImpl<SaveNfc,Long> implements SaveNfcService {

    private SaveNFCDao saveNFCDao;

    @Autowired
    public void setSaveNFCDao(SaveNFCDao saveNFCDao) {
        this.saveNFCDao = saveNFCDao;
        this.setBaseDAO(saveNFCDao);
    }
}
