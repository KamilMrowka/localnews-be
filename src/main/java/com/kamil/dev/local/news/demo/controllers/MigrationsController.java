package com.kamil.dev.local.news.demo.controllers;

import com.kamil.dev.local.news.demo.parsers.CsvParser;
import com.kamil.dev.local.news.demo.parsers.JsonParser;
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
}
