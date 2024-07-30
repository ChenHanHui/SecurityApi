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
package com.chh.encryption;

import com.chh.util.SecurityData;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public interface Encryption {

    /**
     * 加密
     *
     * @param plainText 明文
     * @return 密文
     */
    SecurityData encrypt(String plainText);

    /**
     * 解密
     *
     * @param securityData 密文
     * @return 明文
     */
    String decrypt(SecurityData securityData);

}
