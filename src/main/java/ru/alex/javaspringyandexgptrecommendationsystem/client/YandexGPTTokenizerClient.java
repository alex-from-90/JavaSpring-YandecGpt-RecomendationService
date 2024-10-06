package ru.alex.javaspringyandexgptrecommendationsystem.client;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class YandexGPTTokenizerClient {

    private final WebClient webClient;

    private final String folderId;

    public YandexGPTTokenizerClient(WebClient webClient, @Value("${yandex.folder.id}") String folderId) {
        this.webClient = webClient;
        this.folderId = folderId;
    }

    public String tokenizeText(String text) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode requestJson = objectMapper.createObjectNode();


            requestJson.put("modelUri", String.format("gpt://%s/yandexgpt", folderId));
            requestJson.put("text", text);


            String requestBody = requestJson.toString();
            System.out.println("Тело запроса: " + requestBody);


            String response = webClient.post()
                    .uri("/foundationModels/v1/tokenize")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Ответ от токенизатора: " + response);

            return extractTokensFromResponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при токенизации текста: " + e.getMessage(), e);
        }
    }

    private String extractTokensFromResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);


            System.out.println("Извлечённые токены: " + rootNode.path("tokens"));


            return rootNode.path("tokens").toString();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при разборе ответа от токенизатора: " + e.getMessage(), e);
        }
    }
}