package ru.alex.javaspringyandexgptrecommendationsystem.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.alex.javaspringyandexgptrecommendationsystem.util.ResponseCleaner;

@Service
public class YandexGPTClient {

    private final WebClient webClient;
    private final String folderId;
    private final String role;
    private final double temperature;
    private final int maxTokens;
    private final ResponseCleaner responseCleaner;

    public YandexGPTClient(WebClient webClient,
                           @Value("${yandex.folder.id}") String folderId,
                           @Value("${yandex.role}") String role,
                           @Value("${yandex.temperature}") double temperature,
                           @Value("${yandex.maxTokens}") int maxTokens,
                           ResponseCleaner responseCleaner) {
        this.webClient = webClient;
        this.folderId = folderId;
        this.role = role;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.responseCleaner = responseCleaner;
    }

    public String generateText(String text) {
        try {
            String requestBody = getRequestJson(text);
            System.out.println("Отправляем запрос в GPT: " + requestBody);


            String response = webClient.post()
                    .uri("/foundationModels/v1/completion")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Ответ от API: " + response);


            return responseCleaner.cleanResponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке запроса: " + e.getMessage(), e);
        }
    }

    private String getRequestJson(String text) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestJson = objectMapper.createObjectNode();
        requestJson.put("modelUri", String.format("gpt://%s/yandexgpt-lite", folderId));

        ObjectNode completionOptions = objectMapper.createObjectNode();
        completionOptions.put("stream", false);
        completionOptions.put("temperature", temperature);
        completionOptions.put("maxTokens", maxTokens);
        requestJson.set("completionOptions", completionOptions);

        ArrayNode messages = objectMapper.createArrayNode();
        ObjectNode message = objectMapper.createObjectNode();
        message.put("role", role);
        message.put("text", text);
        messages.add(message);
        requestJson.set("messages", messages);

        return requestJson.toString();
    }
}