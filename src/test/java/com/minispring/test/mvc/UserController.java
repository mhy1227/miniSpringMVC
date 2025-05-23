package com.minispring.test.mvc;

import com.minispring.core.annotation.Controller;
import com.minispring.core.annotation.RequestMapping;
import com.minispring.core.annotation.RequestParam;

@Controller
public class UserController {
    
    @RequestMapping("/user.do")
    public String getUser(@RequestParam("id") Integer id) {
        return "user-" + id;
    }
    
    @RequestMapping("/hello.do")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }
} 