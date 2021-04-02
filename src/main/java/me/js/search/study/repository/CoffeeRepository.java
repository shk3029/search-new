package me.js.search.study.repository;

import me.js.search.study.domain.Coffee;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CoffeeRepository {

    private Map<String, Coffee> coffeeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        coffeeMap.put("latte", Coffee.makeCoffee("latte", 3500));
        coffeeMap.put("mocha", Coffee.makeCoffee("mocha", 4000));
        coffeeMap.put("americano", Coffee.makeCoffee("americano", 2000));
    }

    public int getPriceByName(String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return coffeeMap.get(name).getPrice();
    }
}
