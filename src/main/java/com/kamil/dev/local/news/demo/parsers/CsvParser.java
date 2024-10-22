package com.kamil.dev.local.news.demo.parsers;

import com.kamil.dev.local.news.demo.dao.entities.CityEntity;
import com.kamil.dev.local.news.demo.dao.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CsvParser {
    private final CityRepository cityRepository;
    public void parse() {
        ClassLoader classLoader = getClass().getClassLoader();
        String line = "";
        List<String> values = new ArrayList<>();
        try (
            InputStream inputStream = classLoader.getResourceAsStream("static/uscities.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {

            // skip first line with columns names
            reader.readLine();
            while((line = reader.readLine()) != null) {
                values = Arrays.stream(line.split(",")).toList();
                CityEntity city = new CityEntity();
                city.setName(values.getFirst().replace("\"", ""));
                city.setStateName(values.get(3).replace("\"", ""));

                cityRepository.save(city);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
