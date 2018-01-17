package com.zjr.technologytest.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: ZhangJiaRen
 * @Date: Created in 9:10 2018/1/17
 * @Description:
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "姓名")
    private String name;


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
