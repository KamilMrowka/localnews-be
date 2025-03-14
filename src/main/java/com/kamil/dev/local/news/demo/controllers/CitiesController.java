package com.kamil.dev.local.news.demo.controllers;

import com.kamil.dev.local.news.demo.dao.entities.CityEntity;
import com.kamil.dev.local.news.demo.dao.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("api/v1/cities")
public class CitiesController {
    private final CityRepository cityRepository;
    @GetMapping
    public List<CityEntity> getCities(@RequestParam String query) {
        return cityRepository.findByNameContainingIgnoreCase(query);
    }
}
