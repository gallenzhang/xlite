package com.xlite.service;

import com.xlite.bean.PersonBean;

import java.util.Date;

/**
 * <p>
 * test service
 * </p>
 *
 * @author gallenzhang
 * @since 2020/4/4
 **/
public interface TestService {

    /**
     * 测试方法
     * @param personBean
     * @param date
     * @param number
     * @param score
     * @return
     */
    String testMethod(PersonBean personBean, Date date,long number,int[][] score);

    /**
     * 空参方法
     * @return
     */
    PersonBean buildPerson();
}
