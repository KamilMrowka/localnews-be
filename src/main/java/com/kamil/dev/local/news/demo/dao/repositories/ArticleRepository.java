package com.kamil.dev.local.news.demo.dao.repositories;

import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    boolean existsArticleEntityByTitle(String title);
    List<ArticleEntity> findArticleEntitiesByCityId(Long cityId);

    List<ArticleEntity> findFirst10ByOrderById();


    List<ArticleEntity> findFirst50ByOrderById();
    List<ArticleEntity> findTop50ByOrderByIdDesc();

    List<ArticleEntity> findTop10ByOrderByIdDesc();

    List<ArticleEntity> findFirstBy();

    Optional<ArticleEntity> findArticleEntityById(long id);

}
