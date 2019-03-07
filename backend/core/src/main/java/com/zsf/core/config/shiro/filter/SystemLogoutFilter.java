package com.zsf.core.config.shiro.filter;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Locale;


/**
 * @author yyq
 */
public class SystemLogoutFilter extends LogoutFilter {

    private final String redirectUrl = "/api/user/error";

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        //在这里执行退出系统前需要清空的数据

        Subject subject = getSubject(request, response);

        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面

        if (this.isPostOnlyLogout() && !WebUtils.toHttp(request).getMethod().toUpperCase(Locale.ENGLISH).equals("POST")) {
            return this.onLogoutRequestNotAPost(request, response);
        } else {

            try {
                subject.logout();
            } catch (SessionException e) {
            }

            this.issueRedirect(request, response, redirectUrl);

        }

        return false;

    }
}

