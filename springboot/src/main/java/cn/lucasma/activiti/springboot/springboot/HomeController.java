package cn.lucasma.activiti.springboot.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Lucas Ma
 */
@RestController
public class HomeController {

    @RequestMapping("/home")
    public String home(){
        return "Hello World ~~~~~~~~~!";
    }
}
