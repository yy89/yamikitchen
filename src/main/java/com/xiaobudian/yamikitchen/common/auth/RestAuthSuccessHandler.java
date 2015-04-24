package com.xiaobudian.yamikitchen.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.web.dto.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Johnson on 2014/10/27.
 */
@Component(value = "restAuthSuccessHandler")
public class RestAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Inject
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, Result.successResult(new UserResponse(request.getSession().getId(), (User) authentication.getPrincipal())));
        writer.flush();
    }
}
