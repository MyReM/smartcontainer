package com.zsf.core.config.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yyq
 */
@Configuration
@EnableScheduling
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RestTemplateBuilder builder;

    @Autowired
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter;

    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;


//    @Bean
//    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
//        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
//        return openEntityManagerInViewInterceptor;
//    }

    // hibernate校验器
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = builder.build();

        restTemplate.setMessageConverters(messageConverters());

        return restTemplate;
    }

    private List<HttpMessageConverter<?>> messageConverters() {

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        messageConverters.add(byteArrayHttpMessageConverter);

        messageConverters.add(stringHttpMessageConverter);

        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

        formHttpMessageConverter.setCharset(Charset.forName("UTF-8"));

        List<MediaType> mediaTypes = new ArrayList<>();

        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);

        formHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        messageConverters.add(formHttpMessageConverter);

        messageConverters.add(fastJsonHttpMessageConverter);

        return messageConverters;
    }

}
