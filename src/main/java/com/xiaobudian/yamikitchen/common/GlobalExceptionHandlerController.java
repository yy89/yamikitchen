package com.xiaobudian.yamikitchen.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Johnson on 2015/4/23.
 */
@ControllerAdvice
public class GlobalExceptionHandlerController {
    @Inject
    private LocalizedMessageSource localizedMessageSource;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result defaultErrorHandler(HttpServletRequest req, HttpServletResponse res, Exception e) throws Exception {
        if (e instanceof HttpRequestMethodNotSupportedException)
            return Result.failResult(localizedMessageSource.getMessage("http.method.notsupport"));
        if (e instanceof TypeMismatchException)
            return Result.failResult(localizedMessageSource.getMessage("http.type.mismatch"));
        if(e instanceof IllegalArgumentException)
            return Result.failResult(e.getMessage());
        return Result.failResult(localizedMessageSource.getMessage(StringUtils.isEmpty(e.getMessage()) ? "server.internal.error" : e.getMessage()));
    }
}