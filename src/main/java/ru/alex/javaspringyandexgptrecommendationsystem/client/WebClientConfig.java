package ru.alex.javaspringyandexgptrecommendationsystem.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${yandex.api.key}") String apiKey) {
        return WebClient.builder()
                .baseUrl("https://llm.api.cloud.yandex.net")
                .defaultHeader("Authorization", "Api-Key " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}