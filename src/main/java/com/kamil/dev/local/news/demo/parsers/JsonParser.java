package com.kamil.dev.local.news.demo.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamil.dev.local.news.demo.dao.dto.ArticleDTO;
import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import com.kamil.dev.local.news.demo.dao.mappers.ArticleMapper;
import com.kamil.dev.local.news.demo.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JsonParser {

    private final ArticleService articleService;

    public void parse() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse JSON file into List<Person>

            File file1 = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("static/1st_set.json")).getFile());
            File file2 = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("static/2nd_set.json")).getFile());
            List<ArticleDTO> articleDTOList = objectMapper.readValue(file1, new TypeReference<List<ArticleDTO>>() {});
            List<ArticleDTO> secondArticleDTOList = objectMapper.readValue(file2, new TypeReference<List<ArticleDTO>>() {});

            articleDTOList.addAll(secondArticleDTOList);

            List<ArticleEntity> articleList = articleDTOList.stream()
                    .map(ArticleMapper::toEntity)
                    .collect(Collectors.toList());

            articleService.saveArticles(articleList, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
