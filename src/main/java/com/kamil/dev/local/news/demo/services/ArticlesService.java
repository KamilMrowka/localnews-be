package com.kamil.dev.local.news.demo.services;


import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import com.kamil.dev.local.news.demo.dao.repositories.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticlesService {
    @PersistenceContext
    private EntityManager entityManager;
    private final ArticleRepository articleRepository;

    @Transactional
    public void saveArticles(List<ArticleEntity> articles) {

        int batchSize = 100;

        for (int i = 0; i < articles.size(); i++) {
           articleRepository.save(articles.get(i));

           if (i>0 && i % batchSize == 0) {
               entityManager.flush();
               entityManager.clear();
           }
        }

        entityManager.flush();
        entityManager.clear();
    }
}
