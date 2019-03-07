package com.zsf.core.config.web;

import com.zsf.core.utils.LogUtil;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yyq
 */
@Configuration
@EnableAsync
public class ExecutorPoolConfig implements AsyncConfigurer {

    private int corePoolSize = 10;//线程池维护线程的最少数量

    private int maxPoolSize = 30;//线程池维护线程的最大数量

    private int queueCapacity = 8; //缓存队列

    private int keepAlive = 60;//允许的空闲时间

    @Bean(value = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        LogUtil.info("异步线程开始执行");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);

        executor.setKeepAliveSeconds(keepAlive);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        executor.setWaitForTasksToCompleteOnShutdown(true);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return new AsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     * @author hry
     *
     */
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        //手动处理捕获的异常
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            LogUtil.info("Exception message - " + throwable.getMessage());
            LogUtil.info("Method name - " + method.getName());
            for (Object param : obj) {
                LogUtil.info("Parameter value - " + param);
            }
        }
    }
}
