package com.kamil.dev.local.news.demo.dao.repositories;

import com.kamil.dev.local.news.demo.dao.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
    Optional<CityEntity> findCityEntityByStateNameAndName(String stateName, String name);
}
