package com.chh.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author实体类，使用SpringBoot的validation校验
 * <p>
 * 作者博客 <a href="https://blog.csdn.net/xiaohuihui1400">https://blog.csdn.net/xiaohuihui1400</a>
 * </p>
 *
 * @author 陈汉辉
 */
public class Author implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotNull(message = "年龄不能为空")
    @Range(min = 0, max = 150, message = "年龄不合法")
    private Integer age;
    @NotNull(message = "日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date day;
    private String url;

    public @NotBlank(message = "姓名不能为空") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "姓名不能为空") String name) {
        this.name = name;
    }

    public @NotNull(message = "年龄不能为空") @Range(min = 0, max = 150, message = "年龄不合法") Integer getAge() {
        return age;
    }

    public void setAge(@NotNull(message = "年龄不能为空") @Range(min = 0, max = 150, message = "年龄不合法") Integer age) {
        this.age = age;
    }

    public @NotNull(message = "日期不能为空") Date getDay() {
        return day;
    }

    public void setDay(@NotNull(message = "日期不能为空") Date day) {
        this.day = day;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
