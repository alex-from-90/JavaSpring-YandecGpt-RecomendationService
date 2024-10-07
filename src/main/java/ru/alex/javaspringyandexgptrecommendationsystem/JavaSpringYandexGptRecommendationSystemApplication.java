package ru.alex.javaspringyandexgptrecommendationsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import ru.alex.javaspringyandexgptrecommendationsystem.client.YandexGPTClient;
import ru.alex.javaspringyandexgptrecommendationsystem.entity.Article;
import ru.alex.javaspringyandexgptrecommendationsystem.interfaces.UserExtractor;
import ru.alex.javaspringyandexgptrecommendationsystem.model.User;
import ru.alex.javaspringyandexgptrecommendationsystem.service.RecommendationService;


import java.util.List;


@SpringBootApplication
public class JavaSpringYandexGptRecommendationSystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaSpringYandexGptRecommendationSystemApplication.class);

    private final YandexGPTClient yandexGPTClient;

    // private final YandexGPTTokenizerClient yandexGPTTokenizerClient;
    public JavaSpringYandexGptRecommendationSystemApplication(YandexGPTClient yandexGPTClient /*, YandexGPTTokenizerClient yandexGPTTokenizerClient*/) {
        this.yandexGPTClient = yandexGPTClient;
        // this.yandexGPTTokenizerClient = yandexGPTTokenizerClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(JavaSpringYandexGptRecommendationSystemApplication.class, args);
    }

    @Bean("userExtractionApplicationRunner")
    @Order(2)
    ApplicationRunner applicationRunner(RecommendationService recommendationService) {
        return args -> {
            try {
                ChatLanguageModel model = prompt -> {
                    String text = prompt.toString();
                    String responseText = yandexGPTClient.generateText(text);
                    AiMessage aiMessage = AiMessage.from(responseText);
                    return new Response<>(aiMessage);
                };

                UserExtractor userExtractor = AiServices.create(UserExtractor.class, model);

                // Токенизатор (закомментированный, как и просили)
                // String tokenizedText = yandexGPTTokenizerClient.tokenizeText(inputText);
                // System.out.println("Токенизированный текст: " + tokenizedText);

                String modifiedText = """
                        Иван Иванов
                        Я обладаю более чем двухлетним опытом в разработке на языке Python, сфокусированным преимущественно на создании телеграмм-ботов и парсеров.
                        Мой опыт включает взаимодействие с YOLO и создание текстовых описаний для проектов.
                        Я также изучаю Golang последние полгода.
                        """;


                User user = userExtractor.extract(modifiedText);
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonUser = objectMapper.writeValueAsString(user);

                logger.info("Полученный JSON: {}", jsonUser);

                List<Article> recommendedArticles = recommendationService.getRecommendedArticles(jsonUser);


                for (Article article : recommendedArticles) {
                    logger.info("Рекомендованная статья: {}", article.getTitle());
                }

                String jsonRecommendedArticles = objectMapper.writeValueAsString(recommendedArticles);
                logger.info("Рекомендованные статьи в формате JSON: {}", jsonRecommendedArticles);


            } catch (Exception e) {
                logger.error("Ошибка при работе с данными", e);
            }
        };
    }
}