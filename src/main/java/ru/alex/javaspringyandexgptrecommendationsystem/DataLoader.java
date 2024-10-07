package ru.alex.javaspringyandexgptrecommendationsystem;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.alex.javaspringyandexgptrecommendationsystem.entity.Article;
import ru.alex.javaspringyandexgptrecommendationsystem.repository.ArticleRepository;

@Configuration
public class DataLoader {

    @Bean
    @Order(1)
    public CommandLineRunner loadData(ArticleRepository repository) {
        return args -> {

            repository.save(new Article(null, "История", "Историческая статья", "Это статья о великих событиях прошлого."));
            repository.save(new Article(null, "Технологии", "Современные технологии", "Это статья о современных технологиях, включая искусственный интеллект."));
            repository.save(new Article(null, "Программирование", "Язык программирования Kotlin", "Это статья о том, как использовать Kotlin для разработки приложений."));
            repository.save(new Article(null, "Путешествия", "Лучшие туристические места", "Это статья о лучших туристических местах в мире."));
            repository.save(new Article(null, "Создание телеграмм-ботов", "Создание телеграмм-ботов", "Эта статья описывает, как создать телеграмм-бот с использованием Python и библиотеки Telegram API."));
            repository.save(new Article(null, "Парсинг данных", "Парсинг данных на Python", "Это статья об автоматическом парсинге данных с веб-сайтов с использованием Python и библиотеки BeautifulSoup."));
            repository.save(new Article(null, "Изучение Golang", "Начало работы с Golang", "Эта статья описывает основы программирования на Go, преимущества использования и примеры."));
            repository.save(new Article(null, "python", "Python для llm", "Эта статья описывает основы программирования на Python, преимущества использования и примеры."));

        };
    }
}