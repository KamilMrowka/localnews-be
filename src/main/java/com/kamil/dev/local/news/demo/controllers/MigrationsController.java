package com.kamil.dev.local.news.demo.controllers;

import com.kamil.dev.local.news.demo.parsers.CsvParser;
import com.kamil.dev.local.news.demo.parsers.JsonParser;
import com.kamil.dev.local.news.demo.processors.CityAssigner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/migrate")
@RequiredArgsConstructor
@RestController
public class MigrationsController {
    private final JsonParser jsonParser;
    private final CsvParser csvParser;
    private final CityAssigner cityAssigner;

    @GetMapping("/load-json")
    public String loadJson () {
       jsonParser.parse();
       return "Loading completed";
    }

    @GetMapping("/load-csv")
    public String loadCsv () {
        csvParser.parse();
        return "Loading completed";
    }

    @GetMapping("/migrate-all-articles")
    public String migrateArticles() {
        cityAssigner.migrateArticles();
        return "Migration succesfull";
    }

    @GetMapping("/migrate-new-articles")
    public String migrateNewArticles() {
        cityAssigner.migrateArticles(true);
        return "Migration succesfull";
    }

}
