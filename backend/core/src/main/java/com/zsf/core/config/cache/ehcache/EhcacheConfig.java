package com.zsf.core.config.cache.ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * @author yyq
 */
@Configuration
@EnableCaching
public class EhcacheConfig {

    private Logger logger = LoggerFactory.getLogger(EhcacheConfig.class);


    /*

     *据shared与否的设置,

     * Spring分别通过CacheManager.create()

     *或new CacheManager()方式来创建一个ehcache基地.

     *

     *也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享)

     *

     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){

        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();

        cacheManagerFactoryBean.setConfigLocation (new ClassPathResource("ehcache.xml"));

        cacheManagerFactoryBean.setShared(true);

        return cacheManagerFactoryBean;

    }

    /**

     * ehcache主要的管理器

     *@param factoryBean

     *@return

     */

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean factoryBean){

        return new EhCacheCacheManager(factoryBean.getObject());

    }

    @Bean
    public KeyGenerator keyGenerator() {

        return new KeyGenerator() {

            // custom cache key
            public static final int NO_PARAM_KEY = 0;
            public static final int NULL_PARAM_KEY = 53;

            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder key = new StringBuilder();
                key.append(target.getClass().getSimpleName()).append(".").append(method.getName()).append(":");

                if (params.length == 0) {
                    return key.append(NO_PARAM_KEY).toString();
                }
                for (Object param : params) {
                    if (param == null) {
                        logger.warn("input null param for Spring cache, use default key={}", NULL_PARAM_KEY);
                        key.append(NULL_PARAM_KEY);
                    } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                        int length = Array.getLength(param);
                        for (int i = 0; i < length; i++) {
                            key.append(Array.get(param, i));
                            key.append(',');
                        }
                    } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
                        key.append(param);
                    } else {
                        logger.warn("Using an object as a cache key may lead to unexpected results. " +
                                "Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass() + "#" + method.getName());
                        key.append(param.hashCode());
                    }
                    key.append('-');
                }

                String finalKey = key.toString();

//                long cacheKeyHash = Hashing.murmur3_128().hashString(finalKey, Charset.defaultCharset()).asLong();
//
//                logger.debug("using cache key={} hashCode={}", finalKey, cacheKeyHash);
                return key.toString();

            }
        };

    }

}
