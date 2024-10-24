package com.kamil.dev.local.news.demo.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "articles")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "article")
    private String article;

    @Column(name = "global")
    private boolean global;

    @Column(name = "city_id")
    private Long cityId;


    /*
        id BIGINT PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        link VARCHAR(255) NOT NULL,
        description text NOT NULL,
        article text NOT NULL,
        global BOOLEAN DEFAULT FALSE,
        city_id BIGINT,
     */
}
