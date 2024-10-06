package ru.alex.javaspringyandexgptrecommendationsystem.interfaces;


import dev.langchain4j.service.UserMessage;
import ru.alex.javaspringyandexgptrecommendationsystem.model.User;


public interface UserExtractor {

    @UserMessage("""
            Извлеките имя, роль, интересы и навыки программирования пользователя из текста ниже.
            Верните JSON с двумя массивами: один для интересов и один для навыков программирования.
            Выдели интересы в массив.
            Если не можешь найти имя пользователя и роль, верните пустой массив.
            В skills указывай только названия языков программирования.
            Вот текст, описывающий пользователя:
            ---
            {{it}}
            """)
    User extract(String text);
}