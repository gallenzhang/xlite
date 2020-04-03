package com.xlite.demo.provider;

import com.xlite.config.ServiceConfig;
import com.xlite.demo.DemoService;

/**
 * @author : gallenzhang
 * @date : 2019/9/8
 * @description :Provider
 */
public class Provider {

    public static void main(String[] args) {
        DemoService demoService = new DemoServiceImpl();
        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>(DemoService.class, demoService);

        //暴露服务
        serviceConfig.export();
    }
}
