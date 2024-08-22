/*
 * Copyright 2024-2099 ChenHanHui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chh.servlet;

import com.chh.servlet.http.InputStreamHttpServletRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 请求流转换为多次读取的请求流 过滤器
 *
 * @author 陈汉辉
 * @since 1.0.0
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 1)  // 优先级最高
public class HttpServletRequestInputStreamFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 上传文件不做解密处理
        if (StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.MULTIPART_FORM_DATA_VALUE)) {
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
