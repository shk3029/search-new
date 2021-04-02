package me.js.search.study.domain;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {
    private final String name;
    private final Random random;

    public Shop(String name) {
        this.name = name;
        this.random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    // 가격 계산 비동기
    public Future<Double> getAsyncPrice(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(
                () -> {
                  double price = calculatePrice(product);
                  futurePrice.complete(price);
                }
        ).start();
        return futurePrice;
    }

    // 좀 더 간결하게 - supplyAsync
    public Future<Double> getPrice(String price) {
        return CompletableFuture.supplyAsync(()->calculatePrice(price));
    }



    private double calculatePrice(String product) {
        delay_1s();
        System.out.println("calculatePrice");
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public String getName() {
        return name;
    }

    private static void delay_1s() {
        int delay = 1000;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
