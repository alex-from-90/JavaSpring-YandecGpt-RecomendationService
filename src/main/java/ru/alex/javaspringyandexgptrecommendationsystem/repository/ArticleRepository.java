package ru.alex.javaspringyandexgptrecommendationsystem.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alex.javaspringyandexgptrecommendationsystem.entity.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE " +
            "(LOWER(a.tag) = LOWER(:interest) " +
            "OR LOWER(a.tag) LIKE LOWER(CONCAT('%', :interest, '%'))) " +
            "OR (LOWER(a.tag) = LOWER(:skill) " +
            "OR LOWER(a.tag) LIKE LOWER(CONCAT('%', :skill, '%')))")
    List<Article> findByTagsOrSimilarity(@Param("interest") String interest,
                                         @Param("skill") String skill,
                                         Pageable pageable);

}