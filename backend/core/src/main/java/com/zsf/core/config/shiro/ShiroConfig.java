package com.zsf.core.config.shiro;

import com.zsf.core.config.shiro.filter.RolesAuthorizationFilter;
import com.zsf.core.config.shiro.filter.SystemLogoutFilter;
import com.zsf.core.service.RoleService;
import com.zsf.core.service.UserService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.io.DefaultSerializer;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yyq
 */
@Configuration
@Order(value = 51)
public class ShiroConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private Long sessionTimeout = 1800000L;

    @Bean(name = "filterRegistrationBean1")
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new DelegatingFilterProxy("shiroFilter"));

        filterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");

        filterRegistrationBean.setEnabled(true);

        filterRegistrationBean.addUrlPatterns("/");

        return filterRegistrationBean;
    }


    @Bean(value = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new HashMap<>();

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/api/user/error");

        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");

        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/api/user/logout", "logout");

        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");

        filterChainDefinitionMap.put("/api/label/result", "anon");

        filterChainDefinitionMap.put("/websocket/*", "anon");

        filterChainDefinitionMap.put("/api/file/forService/*", "anon");

        filterChainDefinitionMap.put("/api/scan/*", "anon");

        filterChainDefinitionMap.put("/api/user/login", "anon");

        filterChainDefinitionMap.put("/api/user/exist", "anon");

        filterChainDefinitionMap.put("/api/template/download", "anon");

        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        Map<String, Filter> filterMap = new HashMap<>();

        AuthorizationFilter roleFilter = new RolesAuthorizationFilter();
        FormAuthenticationFilter authcFilter = new com.zsf.core.config.shiro.filter.AuthorizationFilter();
        SystemLogoutFilter logoutFilter = new SystemLogoutFilter();

        filterMap.put("role", roleFilter);
        filterMap.put("authc", authcFilter);
        filterMap.put("logout", logoutFilter);

        shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");

        // 散列的次数
        hashedCredentialsMatcher.setHashIterations(1024);

        return hashedCredentialsMatcher;
    }

    @Bean(name = "sessionValidationScheduler")
    public ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(900000);

        //scheduler.setSessionManager(defaultWebSessionManager());

        return scheduler;
    }

    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = new MemoryConstrainedCacheManager();

        return cacheManager;
    }


    @Bean
    public SecurityManager securityManager(CacheManager cacheManager, SessionDAO sessionDAO) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(shiroRealm(cacheManager));


        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager(sessionDAO));


        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager);

        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }

    @Bean
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public AuthorizingRealm shiroRealm(CacheManager cacheManager){
        ShiroAuthRealm shiroRealm = new ShiroAuthRealm();

        shiroRealm.setCacheManager(cacheManager);

        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return shiroRealm;
    }

    //自定义sessionManager
    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new WebSessionManager();

        sessionManager.setDeleteInvalidSessions(true);

        sessionManager.setSessionValidationInterval(sessionTimeout);

        sessionManager.setGlobalSessionTimeout(sessionTimeout);

        sessionManager.setSessionDAO(sessionDAO);

        return sessionManager;
    }

    @Bean
    public SessionDAO sessionDAO() {
        SessionDAO sessionDAO = new EnterpriseCacheSessionDAO();

        return sessionDAO;
    }

    /**
     * Shiro生命周期处理器
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */

//    @Bean
//    @DependsOn({"lifecycleBeanPostProcessor"})
//    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        advisorAutoProxyCreator.setProxyTargetClass(true);
//        return advisorAutoProxyCreator;
//    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(CacheManager cacheManager, SessionDAO sessionDAO) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager(cacheManager, sessionDAO));
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 注册全局异常处理
     * @return
     */
//    @Bean(name = "exceptionHandler")
//    public HandlerExceptionResolver handlerExceptionResolver() {
//        return null;
////        return new RestExceptionHandler();
//    }

    /**
     * cookie对象;
     * @return
     */

    public SimpleCookie rememberMeCookie(){
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");

        // 记住我cookie生效时间30天 ,单位秒;

        simpleCookie.setMaxAge(2592000);

        return simpleCookie;
    }

    /**
     * cookie管理对象;记住我功能
     * @return
     */
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();

        cookieRememberMeManager.setCookie(rememberMeCookie());

        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));

        Serializer<PrincipalCollection> serializer = new DefaultSerializer<>();

        cookieRememberMeManager.setSerializer(serializer);
        return cookieRememberMeManager;
    }
}
