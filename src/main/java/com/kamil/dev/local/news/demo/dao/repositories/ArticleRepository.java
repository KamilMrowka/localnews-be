package com.kamil.dev.local.news.demo.dao.repositories;

import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

}
