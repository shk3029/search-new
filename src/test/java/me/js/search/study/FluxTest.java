package me.js.search.study;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FluxTest {

    @Test
    void createAFlux_just() {
        Flux<String> flux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");
        flux.subscribe(f -> log.info("Here's some fruit :  " + f)); // subscribe를 호출하는 즉시 데이터가 전달되기 시작

        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    void createAtFlux_fromArray() {
        String[] fruits = new String[] {"Apple", "Orange", "Grape", "Banana", "Strawberry"};

        Flux<String> flux = Flux.fromArray(fruits);

        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    void mergeFluxes() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
                .just("Apples", "Orange")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Apples")
                .expectNext("Kojak")
                .expectNext("Orange")
                .expectNext("Barbossa")
                .verifyComplete();
    }

    @Test
    public void buffer() {
        Flux<String> fruitFlux = Flux.just(
                "apple", "orange", "banana", "kiwi", "strawberry");

        Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

        StepVerifier
                .create(bufferedFlux)
                .expectNext(Arrays.asList("apple", "orange", "banana"))
                .expectNext(Arrays.asList("kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    public void buffer_log() {
        Flux.just("apple", "orange", "banana", "kiwi", "strawberry")
                .buffer(3)
                .flatMap(x ->
                        Flux.fromIterable(x)
                                .map(y->y.toUpperCase())
                                .subscribeOn(Schedulers.parallel())
                                .log()
                ).subscribe();

    }
}
