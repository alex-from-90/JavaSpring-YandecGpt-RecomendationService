package ru.alex.javaspringyandexgptrecommendationsystem.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseCleaner {

    public String cleanResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            JsonNode rootNode = objectMapper.readTree(response);


            JsonNode resultNode = rootNode.path("result");
            if (resultNode.isMissingNode()) {
                throw new RuntimeException("Ответ не содержит поле 'result'");
            }

            JsonNode alternativesNode = resultNode.path("alternatives");
            if (!alternativesNode.isArray() || alternativesNode.isEmpty()) {
                throw new RuntimeException("Ответ не содержит корректного массива 'alternatives'");
            }

            JsonNode firstAlternative = alternativesNode.get(0);
            if (firstAlternative == null || firstAlternative.isMissingNode()) {
                throw new RuntimeException("Ответ не содержит корректного альтернативного варианта");
            }


            String rawText = firstAlternative.path("message").path("text").asText();
            rawText = rawText.replaceAll("```", "").trim();  // Удаляем обратные кавычки


            objectMapper.readTree(rawText);

            return rawText;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при разборе ответа: " + e.getMessage(), e);
        }
    }
}