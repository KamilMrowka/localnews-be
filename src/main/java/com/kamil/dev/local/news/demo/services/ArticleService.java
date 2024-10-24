package com.kamil.dev.local.news.demo.services;


import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import com.kamil.dev.local.news.demo.dao.repositories.ArticleRepository;
import com.kamil.dev.local.news.demo.processors.CityAssigner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    @PersistenceContext
    private EntityManager entityManager;
    private final ArticleRepository articleRepository;
    private final int PAGE_SIZE = 10;

    public List<ArticleEntity> getArticles(long city_id, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        System.out.println(city_id);
        System.out.println(page);

        return articleRepository.findByCityIdOrderByIdDesc(city_id, pageable);
    }

    public List<ArticleEntity> getArticles(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return articleRepository.findByGlobalOrderByIdDesc(true, pageable);
    }


    @Transactional
    public void saveArticles(List<ArticleEntity> articles) {
        saveArticles(articles, false);
    }

    @Transactional
    public void saveArticles(List<ArticleEntity> articles, boolean onlyNewArticles) {

        int batchSize = 100;

        for (int i = 0; i < articles.size(); i++) {
            if (onlyNewArticles) {
                boolean articleExists = articleRepository.existsArticleEntityByTitle(articles.get(i).getTitle());
                if(!articleExists) {
                    articleRepository.save(articles.get(i));
                }
            } else {
                articleRepository.save(articles.get(i));
            }

            if (i>0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }


}
