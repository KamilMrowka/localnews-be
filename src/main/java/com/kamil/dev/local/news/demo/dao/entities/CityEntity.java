package com.kamil.dev.local.news.demo.dao.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cities")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "has_articles")
    private boolean hasArticles;


   /*
        id BIGINT PRIMARY KEY,
        name VARCHAR(255),
        state_name VARCHAR(255),
        has_articles BOOLEAN DEFAULT FALSE
    */
}
