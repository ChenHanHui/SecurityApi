package com.chh.servlet;

import com.chh.servlet.http.InputStreamHttpServletRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * 请求流转换为多次读取的请求流 过滤器
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 1)  // 优先级最高
public class HttpServletRequestInputStreamFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //上传文件不做解密处理
        if ("multipart/form-data".equals(((HttpServletRequest) request).getHeader("Content-Type"))) {
            chain.doFilter(request, response);
        } else {
            // 转换为可以多次获取流的request
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            InputStreamHttpServletRequestWrapper inputStreamHttpServletRequestWrapper = new InputStreamHttpServletRequestWrapper(httpServletRequest);
            // 放行
            chain.doFilter(inputStreamHttpServletRequestWrapper, response);
        }
    }
}
