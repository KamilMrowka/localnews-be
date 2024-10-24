package com.kamil.dev.local.news.demo.dao.repositories;

import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;



import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    boolean existsArticleEntityByTitle(String title);
    List<ArticleEntity> findArticleEntitiesByCityIdAndGlobal(Long cityId, boolean global);

    Optional<ArticleEntity> findArticleEntityById(long id);

    List<ArticleEntity> findByGlobalOrderByIdDesc(boolean global, Pageable pageable);

    List<ArticleEntity> findByCityIdOrderByIdDesc(Long cityId, Pageable pageable);

}
