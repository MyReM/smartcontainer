package com.zsf.core.utils;

//import com.easycode.api.core.Bean;
//import com.easycode.api.core.Property;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author YeYuanQing
 * @date 2017/9/29
 * Spring的工具类，用来获得Spring中的bean
 * 实现接口ApplicationContextAware
 * 说明：实现该接口的setApplicationContext(ApplicationContext context)方法，并保存ApplicationContext 对象。
 * Spring初始化时，会通过该方法将ApplicationContext对象注入。
 */
@Component
public class SpringContext implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;
    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        checkApplicationContext();
        return (T) applicationContext.getBean(clazz);
    }

    /***
     * 类似于getBean(String name)只是在参数中提供了需要返回到的类型。
     *
     * @param name
     * @param requiredType
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static <T> boolean containsBean(Class<T> clazz) {

        Map<String, T> map = null;
        try {
            map = getBeansOfType(clazz);
        } catch (BeansException e) {
            return false;
        }
        return !ObjectUtils.isEmpty(map);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) throws BeansException {

        Map<String, T> map = applicationContext.getBeansOfType(clazz);

        return map;
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringBeanUtils");
        }
    }

    public static String getRootRealPath() {
        String rootRealPath = "";
        try {
            rootRealPath = getApplicationContext().getResource("").getFile().getAbsolutePath();
        } catch (IOException e) {
            //获取系统根目录失败
        }
        return rootRealPath;
    }

    public static String getResourceRootRealPath() {
        String rootRealPath = "";
        try {
            rootRealPath = new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
        } catch (IOException e) {
            //获取系统根目录失败
        }
        return rootRealPath;
    }

    public static int count(){
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        int beanCount = defaultListableBeanFactory.getBeanDefinitionCount();
        return beanCount;
    }

//    public static void registerBean(Bean bean) throws ClassNotFoundException {
//        //将applicationContext转换为ConfigurableApplicationContext
//        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
//        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
//        if (!defaultListableBeanFactory.containsBean(bean.getBeanName())){
//            // 通过BeanDefinitionBuilder创建bean定义
//            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Class.forName(bean.getBeanClass()));
//            Set<Property> properties = bean.getProperties();
//            if (properties != null && properties.size() > 0) {
//                Iterator<Property> beanProps = properties.iterator();
//                while (beanProps.hasNext()) {
//                    Property property = beanProps.next();
//                    if (property.isObject()) {
//                        beanDefinitionBuilder.addPropertyReference(property.getName(), property.getValue());
//                    }else {
//                        beanDefinitionBuilder.addPropertyValue(property.getName(),property.getValue());
//                    }
//                }
//            }
//            AbstractBeanDefinition definition = beanDefinitionBuilder.getBeanDefinition();
//            //设置类
//            definition.setBeanClass(Class.forName(bean.getBeanClass()));
//            //设置scope
//            definition.setScope(bean.getScope());
//            //设置是否懒加载
//            definition.setLazyInit(bean.isLazyInit());
//            //设置是否可以被其他对象自动注入
//            definition.setAutowireCandidate(bean.isAutowireCandidate());
//            defaultListableBeanFactory.registerBeanDefinition(bean.getBeanName(),definition);
//        }
//
//    }

    public static <T> boolean registerBean(T bean) throws ClassNotFoundException {
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        if (!defaultListableBeanFactory.containsBean(bean.getClass().getName())){
            BeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClassName(bean.getClass().getName());
            beanDefinition.setAutowireCandidate(true);
            defaultListableBeanFactory.registerBeanDefinition(bean.getClass().getName(), beanDefinition);
            return true;
        } else {
            return false;
        }

    }

    public static void unregisterBean(String beanName){
//        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
//        BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        if (defaultListableBeanFactory.containsBean(beanName)) {
            defaultListableBeanFactory.removeBeanDefinition(beanName);
        }
    }

    @Override
    public void destroy() throws Exception {
        SpringContext.cleanApplicationContext();
    }
}
