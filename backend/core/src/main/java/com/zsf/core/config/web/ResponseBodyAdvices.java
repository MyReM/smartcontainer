package com.zsf.core.config.web;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author YeYuanQing
 * @date 2017/10/18
 */
@RestController
@ControllerAdvice
public class ResponseBodyAdvices implements ResponseBodyAdvice {

    private List<Class> list = new LinkedList();

    public ResponseBodyAdvices() {
        list.add(ResponseEntity.class);
        list.add(Message.class);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        Method method= methodParameter.getMethod();
        String typeName = method.getReturnType().getTypeName();

        if (Objects.equals(typeName,"void")){
            return true;
        }

        Class type = null;
        try {
            type = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Class filter : list){

            if (type != null && filter.equals(type)){
                return false;
            }
        }
        return true;

    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Message message = Message.builder().code(Message.Code.SUCCESS).message("success").data(o).build();
        return message;
    }
}
