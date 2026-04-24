package com.hestia.api.controller;

import com.hestia.api.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {

    @Autowired
    private HelloWorldService helloWorldService;

    @GetMapping
    public String helloWorld() {
        return helloWorldService.helloWorld();
    }

    @PostMapping("/{id}")
    public String helloWorldPost(@PathVariable String id, @RequestBody String body) {
        return "Hello World ";
    }
}
