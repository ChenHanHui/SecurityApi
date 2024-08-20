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
package com.chh.util;

import com.chh.constant.SecurityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈汉辉
 * @since 1.0.2
 */
public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger("SecurityUtils");

    public static String getRequestClientPublicKey() {
        HttpServletRequest request = ServletUtils.getRequest();
        Object publicKey = request.getAttribute(SecurityConstant.CLIENT_PUBLIC_KEY);
        if (publicKey == null || StringUtils.isBlank(publicKey.toString())) {
            log.error("Please set request.setAttribute(SecurityConstant.CLIENT_PUBLIC_KEY, clientPublicKey) " +
                    "in @ModelAttribute");
            throw new SecurityException("Request body decryption failed");
        }
        return publicKey.toString();
    }

}
