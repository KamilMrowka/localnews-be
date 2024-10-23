package com.kamil.dev.local.news.demo.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamil.dev.local.news.demo.dao.dto.AssigningCityDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GptResponseParser {
    public List<AssigningCityDTO> parseGptResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<AssigningCityDTO> assigningCityDTOS = new ArrayList<>();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode articlesNode = rootNode.get("articles");

            for (JsonNode article : articlesNode) {
                long id = article.get("id").asLong();
                boolean global = article.get("global").asBoolean();
                String cityName = article.get("city_name").asText();
                String stateName = article.get("state_name").asText();

                AssigningCityDTO assigningCityDTO = new AssigningCityDTO();
                assigningCityDTO.setId(id);
                assigningCityDTO.setGlobal(global);
                assigningCityDTO.setCityName(cityName);
                assigningCityDTO.setStateName(stateName);

                assigningCityDTOS.add(assigningCityDTO);
            }
            return assigningCityDTOS;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
