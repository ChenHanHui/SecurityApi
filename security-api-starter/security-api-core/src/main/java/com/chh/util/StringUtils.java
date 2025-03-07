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

import com.chh.converte.Convert;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class StringUtils {

    public StringUtils() {
    }

    public static boolean isNull(String s) {
        return s == null;
    }

    public static boolean isNotNull(String s) {
        return !isNull(s);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static String join(Object[] array, String delimiter) {
        return array == null ? null : join(array, delimiter, 0, array.length);
    }

    public static String join(Object[] array, String delimiter, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else if (endIndex - startIndex <= 0) {
            return "";
        } else {
            StringJoiner joiner = new StringJoiner(toStringNullIfDefaultEmpty(delimiter));

            for (int i = startIndex; i < endIndex; ++i) {
                joiner.add(toStringNullIfDefaultEmpty(array[i]));
            }

            return joiner.toString();
        }
    }

    public static String toStringNullIfDefaultEmpty(Object obj) {
        return Objects.toString(obj, "");
    }

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -&gt; this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -&gt; this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -&gt; this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return 结果
     */
    public static String format(final String strPattern, final Object... argArray) {
        if (StringUtils.isBlank(strPattern) || (argArray == null || argArray.length == 0)) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();

        // 初始化定义好的长度以获得更好的性能
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimIndex = strPattern.indexOf("{}", handledPosition);
            if (delimIndex == -1) {
                if (handledPosition == 0) {
                    return strPattern;
                } else { // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                    sbuf.append(strPattern, handledPosition, strPatternLength);
                    return sbuf.toString();
                }
            } else {
                if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == '\\') {
                    if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == '\\') {
                        // 转义符之前还有一个转义符，占位符依旧有效
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(Convert.utf8Str(argArray[argIndex]));
                        handledPosition = delimIndex + 2;
                    } else {
                        // 占位符被转义
                        argIndex--;
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append('{');
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    // 正常占位符
                    sbuf.append(strPattern, handledPosition, delimIndex);
                    sbuf.append(Convert.utf8Str(argArray[argIndex]));
                    handledPosition = delimIndex + 2;
                }
            }
        }
        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPattern.length());

        return sbuf.toString();
    }

    /**
     * 将字符串分割成指定长度的子字符串数组。
     *
     * @param input 要分割的字符串。
     * @param length 每个子字符串的长度。
     * @return 分割后的子字符串数组。
     */
    public static String[] splitStringByLength(String input, int length) {
        // 检查输入字符串是否为空或长度为0，或者分割长度是否小于1
        if (input == null || input.isEmpty() || length < 1) {
            return new String[0]; // 返回空数组
        }

        // 计算需要多少个分割
        int arrayLength = (int) Math.ceil((double) input.length() / length);
        String[] result = new String[arrayLength];

        // 分割字符串
        for (int i = 0; i < arrayLength; i++) {
            int startIndex = i * length;
            int endIndex = Math.min(startIndex + length, input.length());
            result[i] = input.substring(startIndex, endIndex);
        }

        return result;
    }

}
