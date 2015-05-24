package com.xiaobudian.yamikitchen.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaobudian.yamikitchen.common.LocalizedMessageSource;
import com.xiaobudian.yamikitchen.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Johnson on 2014/10/28.
 */
@Component(value = "restAuthFailureHandler")
public class RestAuthFailureHandler implements AuthenticationFailureHandler {
    @Inject
    private ObjectMapper mapper;
    @Inject
    private LocalizedMessageSource localizedMessageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, Result.failResult(localizedMessageSource.getMessage("user.login.user_password.error")));
        writer.flush();
    }
}
