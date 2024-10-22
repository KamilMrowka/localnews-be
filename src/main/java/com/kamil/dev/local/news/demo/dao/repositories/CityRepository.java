package com.kamil.dev.local.news.demo.dao.repositories;

import com.kamil.dev.local.news.demo.dao.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
}
