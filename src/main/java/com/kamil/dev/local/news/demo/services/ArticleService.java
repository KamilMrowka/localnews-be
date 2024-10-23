package com.kamil.dev.local.news.demo.services;


import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import com.kamil.dev.local.news.demo.dao.repositories.ArticleRepository;
import com.kamil.dev.local.news.demo.processors.CityAssigner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ArticleService {
    @PersistenceContext
    private EntityManager entityManager;
    private final ArticleRepository articleRepository;

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
