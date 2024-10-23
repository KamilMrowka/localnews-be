package com.kamil.dev.local.news.demo.dao.dto;

import lombok.Data;

@Data
public class AssigningCityDTO {
    private long id;
    private boolean global;
    private String cityName;
    private String stateName;
}
