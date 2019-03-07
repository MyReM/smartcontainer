package com.zsf.core.config.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zsf.core.config.web.Message;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author yyq
 */
public class AuthorizationFilter extends FormAuthenticationFilter {

    private Message message = new Message();

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        String url = WebUtils.toHttp(request).getRequestURL().toString();

        Subject subject = getSubject(request, response);


        if (Objects.nonNull(subject)) {

            if (subject.isAuthenticated()) {
                return Boolean.TRUE;
            }else {

                return Boolean.FALSE;
            }
        }

        return false;

    }

}
