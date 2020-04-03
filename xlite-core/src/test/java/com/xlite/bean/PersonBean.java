package com.xlite.bean;

import java.io.Serializable;

/**
 * <p>
 * Person Bean
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public class PersonBean implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

    public PersonBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "PersonBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
