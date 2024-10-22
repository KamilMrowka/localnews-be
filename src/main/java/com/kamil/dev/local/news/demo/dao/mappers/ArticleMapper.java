package com.kamil.dev.local.news.demo.dao.mappers;

import com.kamil.dev.local.news.demo.dao.dto.ArticleDTO;
import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;

public class ArticleMapper {
    public static ArticleEntity toEntity(ArticleDTO dto) {
        ArticleEntity entity = new ArticleEntity();
        entity.setLink(dto.getLink());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setArticle(dto.getArticle());
        entity.setCityId(null);
        return entity;
    }
}
