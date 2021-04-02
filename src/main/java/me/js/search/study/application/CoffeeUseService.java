package me.js.search.study.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.js.search.study.repository.CoffeeRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CoffeeUseService {

    private final CoffeeRepository coffeeRepository;

    private Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public int getPrice(String name) {
        log.info("동기 조회");
        return coffeeRepository.getPriceByName(name);
    }

    public CompletableFuture<Integer> getPriceAsync(String name) {
        log.info("비동기 조회");

        return CompletableFuture.supplyAsync(()-> {
            log.info("Thread Name : {}", Thread.currentThread().getName());
            log.info("SupplyAsync");
            return coffeeRepository.getPriceByName(name);
        }, executor);
    }

    public CompletableFuture<Integer> getDiscountPriceAsync(Integer price) {
        return CompletableFuture.supplyAsync(()->{
            log.info("SupplyAsync2");
            return (int)(price * 0.9);
        }, executor);
    }
}









