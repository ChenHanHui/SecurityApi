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
package com.chh.servlet.http;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 请求流支持多次获取
 *
 * @author 陈汉辉
 * @since 1.0.0
 */
public class InputStreamHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 用于缓存输入流
     */
    private ByteArrayOutputStream cachedBytes;

    public InputStreamHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null) {
            // 首次获取流时，将流放入 缓存输入流 中
            cacheInputStream();
        }

        // 从 缓存输入流 中获取流并返回
        return new CachedServletInputStream(cachedBytes.toByteArray());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 首次获取流时，将流放入 缓存输入流 中
     */
    private void cacheInputStream() throws IOException {
        // 缓存输入流以便多次读取。为了方便, 使用 org.apache.commons IOUtils
        cachedBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    /**
     * 读取缓存的请求正文的输入流
     * <p>
     * 用于根据 缓存输入流 创建一个可返回的
     */
    public static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream input;

        public CachedServletInputStream(byte[] buf) {
            // 从缓存的请求正文创建一个新的输入流
            input = new ByteArrayInputStream(buf);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() {
            return input.read();
        }
    }

}
