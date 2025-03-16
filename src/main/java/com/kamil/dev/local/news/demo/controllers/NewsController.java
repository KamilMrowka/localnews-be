package com.kamil.dev.local.news.demo.controllers;

import com.kamil.dev.local.news.demo.dao.dto.LandingPageResponseDTO;
import com.kamil.dev.local.news.demo.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("news")
public class NewsController {
    private final ArticleService articleService;

    @GetMapping("/local")
    public LandingPageResponseDTO getNews(@RequestParam("city_id") long city_id, @RequestParam("page") int page) {
        LandingPageResponseDTO response = new LandingPageResponseDTO();
        response.setArticles(articleService.getArticles(city_id, page));
        response.setLastPage(articleService.getLastPage(city_id));
        return response;
    }

    @GetMapping()
    public LandingPageResponseDTO getGlobalNews(@RequestParam("page") int page) {
        LandingPageResponseDTO response = new LandingPageResponseDTO();
        response.setArticles(articleService.getArticles(page));
        response.setLastPage(articleService.getLastPage());
        return response;
    }
}