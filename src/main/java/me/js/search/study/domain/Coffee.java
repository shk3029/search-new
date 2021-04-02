package me.js.search.study.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Coffee {

    private String name;
    private int price;

    @Builder
    public Coffee(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Coffee makeCoffee(String name, int price) {
        return Coffee.builder()
                .name(name)
                .price(price)
                .build();
    }
}
