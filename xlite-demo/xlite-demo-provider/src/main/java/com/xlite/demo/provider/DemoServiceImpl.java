package com.xlite.demo.provider;


import com.xlite.demo.DemoService;
import com.xlite.demo.PersonBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : gallenzhang
 * @date : 2019/9/8
 * @description :DemoServiceImpl
 */
public class DemoServiceImpl implements DemoService {


    @Override
    public String sayHello(String name) {
        return "牛逼了，自制RPC调用成功啦！---> " + "hello " + name + "! now time is " + "[" +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]";
    }

    @Override
    public String sayHello(PersonBean personBean) {
        return "碉堡了，PersonBean：" + personBean;
    }
}
