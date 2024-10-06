package ru.alex.javaspringyandexgptrecommendationsystem.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.alex.javaspringyandexgptrecommendationsystem.entity.Article;
import ru.alex.javaspringyandexgptrecommendationsystem.repository.ArticleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final int LIMIT = 3;  // Лимит статей
    private final ArticleRepository articleRepository;
    private final ObjectMapper objectMapper;

    public RecommendationService(ArticleRepository articleRepository, ObjectMapper objectMapper) {
        this.articleRepository = articleRepository;
        this.objectMapper = objectMapper;
    }

    public List<Article> getRecommendedArticles(String userJson) throws Exception {

        JsonNode jsonNode = objectMapper.readTree(userJson);
        List<String> interests = objectMapper.convertValue(jsonNode.get("interests"), List.class);
        List<String> skills = objectMapper.convertValue(jsonNode.get("skills"), List.class);

        System.out.println("Интересы пользователя: " + interests);
        System.out.println("Навыки пользователя: " + skills);

        Set<Article> recommendedArticlesSet = new HashSet<>();

        for (String interest : interests) {
            List<Article> articlesByInterest = articleRepository.findByTagsOrSimilarity(interest, skills.get(0), Pageable.ofSize(LIMIT));
            recommendedArticlesSet.addAll(articlesByInterest);

            if (recommendedArticlesSet.size() >= LIMIT) {
                break;
            }
        }

        // Преобразуем в список и ограничиваем до 3 статей
        return recommendedArticlesSet.stream().limit(LIMIT).collect(Collectors.toList());
    }
}