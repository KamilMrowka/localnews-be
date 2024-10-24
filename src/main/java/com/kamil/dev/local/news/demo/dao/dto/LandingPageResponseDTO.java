package com.kamil.dev.local.news.demo.dao.dto;
import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import lombok.Data;

import java.util.List;

@Data
public class LandingPageResponseDTO {
    private List<ArticleEntity> articles;
    private int lastPage;
}
