package com.xlite.demo.provider;


import com.xlite.demo.DemoService;
import com.xlite.demo.PersonBean;

/**
 * @author : gallenzhang
 * @date : 2019/9/8
 * @description :DemoServiceImpl
 */
public class DemoServiceImpl implements DemoService {


    @Override
    public String sayHello(String name) {
        return "牛逼了，RPC调用成功。[" + name + "]";
    }

    @Override
    public String sayHello(PersonBean personBean) {
        return "碉堡了，PersonBean：" + personBean;
    }
}
