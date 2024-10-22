package com.kamil.dev.local.news.demo.processors;

import com.kamil.dev.local.news.demo.dao.repositories.ArticleRepository;
import com.kamil.dev.local.news.demo.dao.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityAssigner {
    private final CityRepository cityRepository;
    private final ArticleRepository articleRepository;
}
