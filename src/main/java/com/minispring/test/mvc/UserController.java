package com.minispring.test.mvc;

import com.minispring.core.annotation.Controller;
import com.minispring.core.annotation.RequestMapping;
import com.minispring.core.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController {
    
    @RequestMapping("/user.do")
    public String getUser(@RequestParam("id") Integer id) {
        return "user-" + id;
    }
    
    @RequestMapping("/hello.do")
    public void hello(
            @RequestParam(value = "name", defaultValue = "World") String name,
            HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("Hello, " + name + "!");
    }
} 