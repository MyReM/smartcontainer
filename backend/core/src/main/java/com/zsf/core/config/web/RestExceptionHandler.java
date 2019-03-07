package com.zsf.core.config.web;
import com.zsf.core.utils.LogUtil;
import com.zsf.core.entity.Log;
import com.zsf.core.service.LogService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @author yeyuanqing
 */
@ResponseBody
@ControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private LogService logService;

    //运行时异常
    @ExceptionHandler(value = RuntimeException.class)
    public Message runtimeExceptionHandler(HttpServletRequest request, RuntimeException ex) {
        Message message = Message.builder().message("运行出错，请联系管理员！").build();
        LogUtil.error("运行时异常:" + ex.toString());
        return this.build(request,ex, message);
    }

    //空指针异常
    @ExceptionHandler(value = NullPointerException.class)
    public Message nullPointerExceptionHandler(HttpServletRequest request, NullPointerException ex) {
        Message message = Message.builder().build();
        LogUtil.error("空指针异常:" + ex.toString());
        ex.printStackTrace();

        return this.build(request,ex, message);
    }
    //类型转换异常
    @ExceptionHandler(value = ClassCastException.class)
    public Message classCastExceptionHandler(HttpServletRequest request, ClassCastException ex) {
        Message message = Message.builder().message("类型转换出错！").build();
        LogUtil.error("类型转换异常" + ex.toString());
        return this.build(request,ex, message);
    }

    //IO异常
    @ExceptionHandler(value = IOException.class)
    public Message iOExceptionHandler(HttpServletRequest request, IOException ex) {
        Message message = Message.builder().build();
        LogUtil.error("IO异常" + ex.toString());
        return this.build(request,ex, message);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public Message noSuchMethodExceptionHandler(HttpServletRequest request, NoSuchMethodException ex) {
        Message message = Message.builder().build();
        LogUtil.error("未知方法异常:" + ex.toString());
        return this.build(request,ex, message);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Message indexOutOfBoundsExceptionHandler(HttpServletRequest request, IndexOutOfBoundsException ex) {
        Message message = Message.builder().build();
        LogUtil.error("数组越界异常:");
        return this.build(request,ex, message);
    }

    //类不存在异常
    @ExceptionHandler(ClassNotFoundException.class)
    public Message classNotFoundExceptionHandler(HttpServletRequest request, ClassNotFoundException ex) {
        Message message = Message.builder().build();
        LogUtil.error("类不存在:" + ex.toString());
        return this.build(request,ex, message);
    }

    //数字转换异常
    @ExceptionHandler(NumberFormatException.class)
    public Message numberFormatExceptionHandler(HttpServletRequest request, NumberFormatException ex) {
        Message message = Message.builder().build();
        LogUtil.error("数字转换异常:" + ex.toString());
        return this.build(request,ex, message);
    }

    //方法参数错误异常
    @ExceptionHandler(IllegalArgumentException.class)
    public Message IllegalArgumentExceptionHandler(HttpServletRequest request, IllegalArgumentException ex) {
        Message message = Message.builder().build();
        LogUtil.error("方法参数错误异常:");
        return this.build(request,ex, message);
    }

    @ExceptionHandler(IllegalStateException.class)
    public Message IllegalArgumentExceptionHandler(HttpServletRequest request, IllegalStateException ex) {
        Message message = Message.builder().build();
        LogUtil.error(ex.toString());
        return this.build(request,ex, message);
    }

    //数学运算异常
    @ExceptionHandler(ArithmeticException.class)
    public Message arithmeticExceptionHandler(HttpServletRequest request, ArithmeticException ex) {
        Message message = Message.builder().build();
        LogUtil.error("数学运算异常");
        return this.build(request,ex, message);
    }

    //没有访问权限异常
    @ExceptionHandler(IllegalAccessException.class)
    public Message IllegalAccessExceptionHandler(HttpServletRequest request, IllegalAccessException ex) {
        Message message = Message.builder().build();
        LogUtil.error(ex.toString());
        return this.build(request,ex, message);
    }

    //文件未找到异常
    @ExceptionHandler(FileNotFoundException.class)
    public Message fileNotFoundExceptionHandler(HttpServletRequest request, FileNotFoundException ex) {
        Message message = Message.builder().message("文件找不到错误！").build();
        LogUtil.error(ex.toString());
        return this.build(request,ex, message);
    }


    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Message requestNotReadable(HttpServletRequest request, HttpMessageNotReadableException ex){
        Message message = Message.builder().build();
        LogUtil.error(ex.toString());
        return this.build(request,ex, message);
    }

    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Message requestMissingServletRequest(HttpServletRequest request, MissingServletRequestParameterException ex){
        Message message = Message.builder().message("缺少请求参数！").build();
        LogUtil.error(ex.toString());
        return this.build(request,ex, message);
    }

    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Message request405(HttpServletRequest request, HttpRequestMethodNotSupportedException ex){
        Message message = Message.builder().message("请求方式不对！").build();
        LogUtil.error(ex.toString());
        return this.build(request,ex, message);
    }
    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public Message request406(HttpServletRequest request, HttpMediaTypeNotAcceptableException ex){
        Message message = Message.builder().build();
        LogUtil.error(ex.toString());
        return this.build(request,ex, message);
    }

    //内存不足错误
    @ExceptionHandler(OutOfMemoryError.class)
    public Message outOfMemoryErrorHandler(HttpServletRequest request, OutOfMemoryError error) {
        Message message = Message.builder().message("服务器内存不足！").build();
        LogUtil.error("内存溢出异常");
        return this.build(request,error, message);
    }

    //未找到类定义错误
    @ExceptionHandler(NoClassDefFoundError.class)
    public Message noClassDefFoundErrorHandler(HttpServletRequest request, NoClassDefFoundError error) {
        Message message = Message.builder().build();
        LogUtil.error("类型未定义异常");
        return this.build(request,error, message);
    }

    // 方法参数校验异常
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Message constraintViolationException(Exception e) {

        Message message = Message.builder().code(Message.Code.ERROR).build();
        if (e.getMessage() != null) {

            String[] messages = e.getMessage().split(",");

            if (messages.length > 1) {

                int index = messages[0].indexOf(":");
                message.setMessage(index != -1 ? messages[0].substring(index + 1).trim() : messages[0]);

            } else {
                int index = e.getMessage().indexOf(":");
                message.setMessage(index != -1 ? e.getMessage().substring(index + 1).trim() : e.getMessage());
            }

        }
        LogUtil.error(message.getMessage());
        return message;
    }


    // Bean 校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Message notValidExceptionHandler(MethodArgumentNotValidException e) throws Exception {

        Message message = Message.builder().code(Message.Code.ERROR).build();

        BindingResult bindingResult = e.getBindingResult();

        if (bindingResult != null && !CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
            message.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else {
            message.setMessage(e.getMessage());
        }
        LogUtil.error(message.getMessage());
        return message;
    }

    /**
     * 登录认证异常
     */
    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public Message authenticationException(HttpServletRequest request, AuthenticationException ex) {
        Message message = Message.builder().message("请求失败，请重新登录").build();
        return this.build(request, ex, message);
    }

    /**
     * 权限异常
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public Message authorizationException(HttpServletRequest request, AuthorizationException ex) {
        Message message = Message.builder().message("请求失败，请检查权限").build();
        return this.build(request, ex, message);
    }

    //
    @ExceptionHandler(Exception.class)
    public Message exceptionHandler(HttpServletRequest request, Exception ex) {
        LogUtil.error(ex.toString());
        Message message = Message.builder().build();
        return this.build(request,ex, message);
    }


    @ExceptionHandler(Throwable.class)
    public Message throwableHandler(HttpServletRequest request, Throwable error) {
        LogUtil.error(error.toString());
        Message message = Message.builder().message("程序出现了不可预料的错误").build();
        return this.build(request,error, message);
    }

    private Message build(HttpServletRequest request, Object object, Message message){

        Log log = Log.builder().type(Log.LogType.BUSINESS).createTime(new Date()).level(Log.LogLevel.ERROR).build();

        if (object instanceof Exception) {
            Exception exception = (Exception) object;
            message.setMessage(Objects.nonNull(message.getMessage()) ? message.getMessage() : exception.getMessage());
            message.setCode(Message.Code.ERROR);

            log.setMessage(exception.getMessage());
        } else {
            Throwable error = (Throwable) object;
            message.setMessage(Objects.nonNull(message.getMessage()) ? message.getMessage() : error.getMessage());
            message.setCode(Message.Code.ERROR);

            log.setMessage(error.getMessage());
        }


        logService.save(log);

        return message;
    }
}
