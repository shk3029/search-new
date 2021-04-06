package me.js.search.application;

import lombok.extern.slf4j.Slf4j;
import me.js.search.dto.SearchRequest;
import me.js.search.dto.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchService {

    @Value("${service.web.uri}")
    private String webUri;

    @Value("${service.app.uri}")
    private String appUri;

    @Value("${service.chat.uri}")
    private String chatUri;

    public List<SearchResponse> getSearchResponse(SearchRequest searchRequest) {
        log.info(">>>>> getSearchResponse >>>>> : {}", searchRequest);

        Mono<SearchResponse> chatBotAData = getChatA(searchRequest.getQuery());
        Mono<SearchResponse> chatBotBData = getChatB(searchRequest.getQuery());
        Mono<SearchResponse> chatBotCData = getChatC(searchRequest.getQuery());
        Mono<SearchResponse> webSearchData = getWebSearchData(searchRequest.getQuery());
        Mono<SearchResponse> appSearchData = getAppSearchData(searchRequest.getQuery());

        log.info("Flux mergeWith Start.... ");
        Flux<SearchResponse> searchResponseFlux = webSearchData.mergeWith(appSearchData)
                            .mergeWith(chatBotAData)
                            .mergeWith(chatBotBData)
                            .mergeWith(chatBotCData);
        log.info("Flux mergeWith End.... {}", searchResponseFlux);

        // Block
        log.info(">>> Block");
        List<SearchResponse> searchResponseList = searchResponseFlux.toStream().collect(Collectors.toList());
        log.info(">>> searchResponse >>> : {}", searchResponseList);

        return searchResponseList;
    }

    private Mono<SearchResponse> getChatA(String query) {
        return getChatBotMono("/A", query);
    }

    private Mono<SearchResponse> getChatB(String query) {
        return getChatBotMono("/B", query);
    }

    private Mono<SearchResponse> getChatC(String query) {
        return getChatBotMono("/C", query);
    }

    private Mono<SearchResponse> getWebSearchData(String query) {
        log.info("WEB 비동기 호출 : {}", query);
        Mono<SearchResponse> webSearchResponseMono = getSearchResponseMono(query, webUri);
        log.info("WEB 비동기 호출완료 - 진행중 ");
        return webSearchResponseMono;
    }

    private Mono<SearchResponse> getAppSearchData(String query) {
        log.info("APP 비동기 호출 : {}", query);
        Mono<SearchResponse> appSearchResponseMono = getSearchResponseMono(query, appUri);
        log.info("APP 비동기 호출완료 - 진행중 ");
        return appSearchResponseMono;
    }

    private Mono<SearchResponse> getChatBotMono(String url, String query) {
        log.info("CHAT 비동기 호출 : {}", query);
        Mono<SearchResponse> chatSearchResponseMono = getSearchResponseMono(query, chatUri + url);
        log.info("CHAT 비동기 호출완료 - 진행중");
        return chatSearchResponseMono;
    }

    private Mono<SearchResponse> getSearchResponseMono(String query, String uri) {
        WebClient webClient = WebClient.builder().build();

        return webClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new SearchRequest(query))
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .timeout(Duration.ofMillis(3000))
                .onErrorResume(e -> {
                    log.error(">> WebClient Call Error >> query : {}, uri : {}, message : {}", query, uri, e.getMessage());
                    return Mono.empty();
                });
    }

}
