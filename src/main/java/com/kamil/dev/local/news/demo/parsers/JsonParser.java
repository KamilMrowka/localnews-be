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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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


            InputStream file1Stream = getClass().getClassLoader().getResourceAsStream("static/1st_set.json");
            InputStream file2Stream = getClass().getClassLoader().getResourceAsStream("static/2nd_set.json");

            if (file1Stream == null || file2Stream == null) {
                throw new FileNotFoundException("File not found in classpath.");
            }

            List<ArticleDTO> articleDTOList = objectMapper.readValue(file1Stream, new TypeReference<List<ArticleDTO>>() {});
            List<ArticleDTO> secondArticleDTOList = objectMapper.readValue(file2Stream, new TypeReference<List<ArticleDTO>>() {});

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
