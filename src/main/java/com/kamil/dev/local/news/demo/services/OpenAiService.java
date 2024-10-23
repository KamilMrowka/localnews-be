package com.kamil.dev.local.news.demo.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${open.ai.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();


    // returns null when response fails
    @Async
    public CompletableFuture<String> getChatCompletions(String prompt) {

        Request request = null;
        try {
            String jsonPayload = mapper.writeValueAsString(new RequestPayload("gpt-4o-mini", prompt));

            // Build request
            request = new Request.Builder()
                    .url(API_URL)
                    .post(RequestBody.create(jsonPayload, MediaType.parse("application/json")))
                    .header("Authorization", "Bearer " + apiKey)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (request != null) {
            // Send request and get response
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonNode rootNode = mapper.readTree(responseBody);

                    return CompletableFuture.completedFuture(rootNode.get("choices").get(0).get("message").get("content").asText());
                } else {
                    System.out.println(response);
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return null;
    }

    private static class RequestPayload {
        public String model;
        public Message[] messages;

//        public Object response_format;
//        public Map<String, Object>[] functions;
//        public String function_call;


        public RequestPayload(String model, String prompt) throws IOException {
            this.model = model;
            this.messages = new Message[]{new Message("user", prompt)};
//            this.response_format = new ObjectMapper().readValue(loadJsonSchema("static/request.schemas/article_response.json"), Object.class);
//            this.functions = new Map[]{loadJsonSchemaAsMap("static/request.schemas/article_response.json")};
//            this.function_call = "auto"; // Or specify the function name
        }
    }

    private static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    private static String loadJsonSchema(String filename) throws IOException {
        InputStream inputStream = OpenAiService.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream == null) {
            throw new IOException("File not found: " + filename);
        }
        String schema = new String(inputStream.readAllBytes());
        inputStream.close();
        return schema;
    }

    private static Map<String, Object> loadJsonSchemaAsMap(String filename) throws IOException {
        InputStream inputStream = OpenAiService.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream == null) {
            throw new IOException("File not found: " + filename);
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> schema = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>(){});
        inputStream.close();
        return schema;
    }

}
