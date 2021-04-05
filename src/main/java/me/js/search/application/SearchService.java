package me.js.search.application;

import lombok.extern.slf4j.Slf4j;
import me.js.search.dto.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class SearchService {

    public List<SearchResponse> getSearchResponse(SearchRequest searchRequest) {
        log.info(">>>>> getSearchResponse >>>>> : {}", searchRequest);

        Mono<SearchResponse> chatBotAData = getChatA(searchRequest.getQuery());
        Mono<SearchResponse> chatBotBData = getChatB(searchRequest.getQuery());
        Mono<SearchResponse> chatBotCData = getChatC(searchRequest.getQuery());
        Mono<SearchResponse> webSearchData = getWebSearchData(searchRequest.getQuery());
        Mono<SearchResponse> appSearchData = getAppSearchData(searchRequest.getQuery());

        log.info("Flux mergeWith Start.... ");
        Flux<SearchResponse> searchResponseFlux =
                webSearchData.mergeWith(appSearchData)
                            .mergeWith(chatBotAData)
                            .mergeWith(chatBotBData)
                            .mergeWith(chatBotCData);

        log.info("Flux mergeWith End.... {}", searchResponseFlux);
        List<SearchResponse> searchResponseList = searchResponseFlux.toStream().collect(Collectors.toList());

        log.info(">>> searchResponse >>> : {}", searchResponseList);
        return searchResponseList;
    }

    private Mono<SearchResponse> getChatA(String query) {
        return getChatBotMono("chatA", query);
    }

    private Mono<SearchResponse> getChatB(String query) {
        return getChatBotMono("chatB", query);
    }

    private Mono<SearchResponse> getChatC(String query) {
        return getChatBotMono("chatC", query);
    }

    private Mono<SearchResponse> getWebSearchData(String query) {
        log.info("WEB 비동기 호출 : {}", query);
        WebClient webClient = WebClient.builder().build();

        Mono<SearchResponse> webSearchResponseMono = webClient
                .post()
                .uri("http://localhost:8082/api/search/web")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new SearchRequest(query))
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .timeout(Duration.ofMillis(3000))
                .onErrorResume(e -> Mono.empty());

        log.info("WEB 비동기 호출완료 - 진행중 ");
        return webSearchResponseMono;
    }

    private Mono<SearchResponse> getAppSearchData(String query) {
        log.info("APP 비동기 호출 : {}", query);
        WebClient webClient = WebClient.builder().build();

        Mono<SearchResponse> appSearchResponseMono = webClient
                .post()
                .uri("http://localhost:8082/api/search/app")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new SearchRequest(query))
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .timeout(Duration.ofMillis(3000))
                .onErrorResume(e -> Mono.empty());

        log.info("APP 비동기 호출완료 - 진행중 ");
        return appSearchResponseMono;
    }


    private Mono<SearchResponse> getChatBotMono(String url, String query) {
        log.info("CHAT 비동기 호출 : {}", query);
        WebClient webClient = WebClient.builder().build();

        Mono<SearchResponse> chatSearchResponseMono = webClient
                .post()
                .uri("http://localhost:8081/api/search/"+url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new SearchRequest(query))
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .timeout(Duration.ofMillis(3000))
                .onErrorResume(e -> Mono.empty());

        log.info("CHAT 비동기 호출완료 - 진행중");
        return chatSearchResponseMono;
    }

}
