package com.xlite.demo.consumer;

import com.xlite.config.ReferenceConfig;
import com.xlite.demo.DemoService;
import com.xlite.demo.PersonBean;

/**
 * @author : gallenzhang
 * @date : 2019/9/8
 * @description :Consumer
 */
public class Consumer {

    public static void main(String[] args) {
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>(DemoService.class);
        //xlite://172.22.57.24:20360
        referenceConfig.setUrl("172.22.57.24:20360");
        DemoService demoService = referenceConfig.get();

        //Rpc远程调用
        String result = demoService.sayHello(new PersonBean("赵铁柱",30,"深圳市南山区"));
        System.out.println("远程调用结果：" + result);
    }
}
