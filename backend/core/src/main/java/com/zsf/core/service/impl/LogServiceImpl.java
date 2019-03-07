package com.zsf.core.service.impl;

import com.zsf.core.dao.LogDAO;
import com.zsf.core.entity.Log;
import com.zsf.core.mapper.LogMapper;
import com.zsf.core.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yyq
 */
@Service
public class LogServiceImpl extends BaseServiceImpl<Log, Long> implements LogService {

    private LogDAO logDAO;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    public void setLogDAO(LogDAO logDAO) {
        this.logDAO = logDAO;
        this.setBaseDAO(logDAO);
    }
}
