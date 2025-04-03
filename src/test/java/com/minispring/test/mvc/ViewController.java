package com.minispring.test.mvc;

import com.minispring.core.annotation.Controller;
import com.minispring.core.annotation.RequestMapping;
import com.minispring.core.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;


@Controller
public class ViewController {
    @RequestMapping("/view/test.do")
    public String testView(
            @RequestParam(value = "message", defaultValue = "Hello JSP") String message,
            HttpServletRequest request) {
        request.setAttribute("message", message);
        return "test";  // 将解析为 /WEB-INF/views/test.jsp
    }
} 