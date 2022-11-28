package com.example.fromzerotoexpert.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JJLin
 * @date 2022/11/28
 */
@RequestMapping("/")
@RestController
public class IndexController {

    @Value("${project.nickname}")
    private String nickname;

    @RequestMapping("FromZerotoExpert")
    public String index() {
        return "嗨，欢迎您来到 " + nickname;
    }
}
