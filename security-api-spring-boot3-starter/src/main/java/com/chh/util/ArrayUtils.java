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

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class ArrayUtils {

    /**
     * 截取数组的一部分
     *
     * @param array               原数组
     * @param startIndexInclusive 开始索引（包含）
     * @param endIndexExclusive   结束索引（不包含）
     * @return 截取后的新数组
     */
    public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new byte[0];
        }

        byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * 将两个数组合并为一个数组
     *
     * @param array1 第一个数组
     * @param array2 第二个数组
     * @return 合并后的新数组
     */
    public static byte[] addAll(byte[] array1, byte[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * 克隆一个数组
     *
     * @param array 要克隆的数组
     * @return 克隆后的新数组
     */
    private static byte[] clone(byte[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
