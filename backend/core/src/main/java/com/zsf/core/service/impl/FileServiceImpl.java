package com.zsf.core.service.impl;

import com.zsf.core.dao.FileDAO;
import com.zsf.core.entity.File;
import com.zsf.core.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yyq
 */
@Service
public class FileServiceImpl extends BaseServiceImpl<File, Long> implements FileService {

    private FileDAO fileDAO;

    @Autowired
    public void setFileDAO(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
        super.setBaseDAO(fileDAO);
    }
}
