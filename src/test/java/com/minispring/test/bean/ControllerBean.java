package com.minispring.test.bean;

import com.minispring.core.annotation.Autowired;

public class ControllerBean {
    @Autowired
    private ServiceBean serviceBean;
    
    public String handleRequest() {
        return serviceBean.sayHello();
    }
    
    public ServiceBean getServiceBean() {
        return serviceBean;
    }
} 