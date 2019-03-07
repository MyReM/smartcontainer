package com.zsf.core.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author YeYuanQing
 * @date 2017/10/17
 */
public class LogUtil {

    private LogUtil(){}

    private static Logger logger() {
        // 最原始被调用的堆栈对象
        StackTraceElement caller = findCaller();
        if(null == caller){
            return LogManager.getLogger(LogUtil.class);
        }

        Logger logger = LogManager.getLogger(caller.getClassName() + "." + caller.getMethodName() + "() Line: " + caller.getLineNumber());

        return logger;
    }

    private static StackTraceElement findCaller() {
        // 获取堆栈信息
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        if(null == callStack){ return null;}

        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;
        // 日志类名称
        String logClassName = LogUtil.class.getName();
        // 循环遍历到日志类标识
        boolean isEachLogClass = false;

        // 遍历堆栈信息，获取出最原始被调用的方法信息
        for (StackTraceElement s : callStack) {
            // 遍历到日志类
            if(logClassName.equals(s.getClassName())) {
                isEachLogClass = true;
            }
            // 下一个非日志类的堆栈，就是最原始被调用的方法
            if(isEachLogClass) {
                if(!logClassName.equals(s.getClassName())) {
                    isEachLogClass = false;
                    caller = s;
                    break;
                }
            }
        }

        return caller;
    }


    public static void debug(Object object) {
        logger().debug(object);
    }

    public static void info(Object object) {
        logger().info(object);
    }

    public static void warn(Object object) {
        logger().warn(object);
    }

    public static void error(Object object) {
        logger().error(object);
    }

    public static void fatal(Object object) {
        logger().fatal(object);
    }

}
