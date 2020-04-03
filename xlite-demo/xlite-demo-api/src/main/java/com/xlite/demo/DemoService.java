package com.xlite.demo;
/**
 * @author : gallenzhang
 * @date : 2019/9/8
 * @description :DemoService
 */
public interface DemoService {

    /**
     * say hello
     * @param name
     * @return
     */
    String sayHello(String name);

    /**
     * say hello
     * @param personBean
     * @return
     */
    String sayHello(PersonBean personBean);
}
