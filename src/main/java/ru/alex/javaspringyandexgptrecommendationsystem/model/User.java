package ru.alex.javaspringyandexgptrecommendationsystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private String role;
    private List<String> interests;
    private List<String> skills;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", interests=" + interests +
                ", skills=" + skills +
                '}';
    }
}