package com.xiaobudian.yamikitchen.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaobudian.yamikitchen.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Johnson on 2015/4/22.
 */

public class UnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, Result.forceLogoutResult("invalid token"));
        writer.flush();
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}