package com.kamil.dev.local.news.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/news")
public class NewsController {

    @GetMapping()
    public String getNews() {
        return "HI, LATEST NEWS FROM: MINESOTTAAAA";
    }
}
