package com.kamil.dev.local.news.demo.controllers;

import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import com.kamil.dev.local.news.demo.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/v1/news")
public class NewsController {
    private final ArticleService articleService;

    @GetMapping("/local")
    public List<ArticleEntity> getNews(@RequestParam long city_id, @RequestParam int page) {
        return articleService.getArticles(city_id, page);
    }

    @GetMapping()
    public List<ArticleEntity> getGlobalNews(int page) {
        return articleService.getArticles(page);
    }
}
