package com.minispring.core.web;

import com.minispring.core.annotation.Controller;
import com.minispring.core.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ViewController {
    @RequestMapping("/view/test")
    public String testView(HttpServletRequest request) {
        return "test";
    }
} 