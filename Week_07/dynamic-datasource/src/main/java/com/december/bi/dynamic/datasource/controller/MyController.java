package com.december.bi.dynamic.datasource.controller;

import com.december.bi.dynamic.datasource.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my")
public class MyController {

    @Autowired
    private MyService myService;

    @GetMapping("/hello")
    public String hello() {
        return myService.hello();
    }

    @GetMapping("/master")
    public String master() {
        return myService.master();
    }

    @GetMapping("/slave")
    public String slave() {
        return myService.slave();
    }

}
