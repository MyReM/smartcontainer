package com.zsf.core.config.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yyq
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {



//    @Bean
//    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
//
//        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
//
//        List<HttpMessageConverter<?>> messageConverters = messageConverters();
//
//        handlerAdapter.setMessageConverters(messageConverters);
//
//        return handlerAdapter;
//    }

//    @Bean
//    public HttpMessageConverters httpMessageConverters(){
//
//        List<HttpMessageConverter<?>> messageConverters = messageConverters();
//
//        return new HttpMessageConverters(messageConverters.stream().toArray(HttpMessageConverter[] :: new));
//
//    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();

        converters.addAll(messageConverters());
    }

    private List<HttpMessageConverter<?>> messageConverters() {

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        messageConverters.add(byteArrayHttpMessageConverter);

        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = fastJsonHttpMessageConverter();

        messageConverters.add(fastJsonHttpMessageConverter);

        messageConverters.add(stringHttpMessageConverter());

        return messageConverters;
    }



    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

        stringHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));

        return stringHttpMessageConverter;
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(){
        //1.需要定义一个Convert转换消息的对象
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();

        //2.添加fastjson的配置信息，比如是否要格式化返回的json数据
        com.alibaba.fastjson.support.config.FastJsonConfig fastJsonConfig = new FastJsonConfigExtend();

        List<SerializerFeature> features = new ArrayList<>();
        features.add(SerializerFeature.WriteDateUseDateFormat);
        features.add(SerializerFeature.WriteEnumUsingToString);
        features.add(SerializerFeature.PrettyFormat);
        features.add(SerializerFeature.WriteNullListAsEmpty);
        features.add(SerializerFeature.WriteMapNullValue);
        features.add(SerializerFeature.DisableCircularReferenceDetect);

        fastJsonConfig.setSerializerFeatures(features.stream().toArray(SerializerFeature[]::new));
        //3.在convert中添加配置信息
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);

        // 解决乱码的问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.TEXT_PLAIN);
        fastMediaTypes.add(MediaType.TEXT_HTML);
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        return fastJsonConverter;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

}
