package com.zsf.core.config.aspect;

import com.zsf.core.entity.Log;
import com.zsf.core.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yyq
 */
@Aspect
@Component
public class WebLogAspect {

    @Autowired
    private LogService logService;

}
