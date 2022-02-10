package xyz.smartrec.center.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "hello controller";
    }

    @RequestMapping("/success")
    public String success() {
        return "success";
    }
}