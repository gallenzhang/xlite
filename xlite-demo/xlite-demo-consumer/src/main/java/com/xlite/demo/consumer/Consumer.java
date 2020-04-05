package com.xlite.demo.consumer;

import com.xlite.config.ReferenceConfig;
import com.xlite.demo.DemoService;
import com.xlite.demo.PersonBean;

import java.io.IOException;

/**
 * @author : gallenzhang
 * @date : 2019/9/8
 * @description :Consumer
 */
public class Consumer {

    public static void main(String[] args) throws IOException {
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>(DemoService.class);
        referenceConfig.setDirectUrl("192.168.1.101:20660");
        DemoService demoService = referenceConfig.get();

        while (true) {
            try {
                Thread.sleep(2000);

                // call remote method
                String hello = demoService.sayHello("gallenzhang");

                // print result
                System.out.println(hello);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
