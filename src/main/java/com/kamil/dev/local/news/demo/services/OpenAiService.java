package com.kamil.dev.local.news.demo.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${open.ai.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    @Async
    public CompletableFuture<String> getChatCompletions(String prompt) {

        Request request = null;
        try {
            String jsonPayload = mapper.writeValueAsString(new RequestPayload("gpt-4o-mini", prompt));

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
                    String cleanedResponseBody = responseBody.replaceAll("```json\n", "").replaceAll("\n```", "");
                    JsonNode rootNode = mapper.readTree(cleanedResponseBody);

                    return CompletableFuture
                            .completedFuture(rootNode.get("choices").get(0).get("message").get("content").asText());
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

        public RequestPayload(String model, String prompt) throws IOException {
            this.model = model;
            this.messages = new Message[] { new Message("user", prompt) };
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

}
