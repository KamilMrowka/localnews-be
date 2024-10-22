package com.kamil.dev.local.news.demo.parsers;

import com.kamil.dev.local.news.demo.dao.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CsvParser {
    private final CityRepository cityRepository;
    public void parseCsv() {

    }
}
