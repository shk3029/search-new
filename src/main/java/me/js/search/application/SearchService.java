package me.js.search.application;

import lombok.extern.slf4j.Slf4j;
import me.js.search.domain.SearchInfo;
import me.js.search.domain.SearchType;
import me.js.search.dto.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchService {

    @Value("${service.totalsearch.uri}")
    private String totalSearchUri;

    @Value("${service.autocomplete.uri}")
    private String autocompleteUri;

    @Value("${service.related.uri}")
    private String relatedUri;

    @Value("${service.popquery.uri}")
    private String popQueryUri;

    @Value("${service.web.uri}")
    private String webUri;

    @Value("${service.web2.uri}")
    private String web2Uri;

    @Value("${service.web3.uri}")
    private String web3Uri;

    @Value("${service.app.uri}")
    private String appUri;


    public Map<String, Object> getSearchResponse(SearchRequest searchRequest) {
        log.info(">>>>> getSearchResponse >>>>> : {}", searchRequest);

        Mono<Map> totalSearchDataMono = getTotalSearchData(searchRequest.getKeyword());
        Mono<Map> autocompleteDataMono = getAutocompleteData(searchRequest.getKeyword());
        Mono<Map> relatedDataMono = getRelatedData(searchRequest.getKeyword());
        Mono<Map> popQueryDataMono = getPopQueryData(searchRequest.getKeyword());

        log.info("Flux mergeWith Start.... ");
        Flux<Map> searchResponseFlux = totalSearchDataMono.mergeWith(autocompleteDataMono)
                .mergeWith(relatedDataMono).mergeWith(popQueryDataMono);
        log.info("Flux mergeWith End.... {}", searchResponseFlux);

        // Block
        Map<String, Object> resultMap = searchResponseFlux.toStream().collect(Collectors.toMap(map -> (String) map.get("type"), map -> map.get("data")));
        log.info(">>> searchResponse >>> : {}", resultMap);
        return resultMap;
    }

    private Mono<Map> getTotalSearchData(String keyword) {
        log.info("통합검색 호출(Non-Blocking) : {}", keyword);
        return getSearchResponseMono(new SearchInfo(keyword, totalSearchUri, SearchType.totalsearch.name()));
    }

    private Mono<Map> getAutocompleteData(String keyword) {
        log.info("자동완성 호출(Non-Blocking) : {}", keyword);
        return getSearchResponseMono(new SearchInfo(keyword, autocompleteUri, SearchType.autocomplete.name()));
    }

    private Mono<Map> getRelatedData(String keyword) {
        log.info("연관검색어 호출(Non-Blocking) : {}", keyword);
        return getSearchResponseMono(new SearchInfo(keyword, relatedUri, SearchType.related.name()));
    }

    private Mono<Map> getPopQueryData(String keyword) {
        log.info("인기검색어 호출(Non-Blocking) : {}", keyword);
        return getSearchResponseMono(new SearchInfo(keyword, popQueryUri, SearchType.popQuery.name()));
    }

    private Mono<Map> getSearchResponseMono(SearchInfo searchInfo) {
        WebClient webClient = WebClient.builder().build();

        return webClient
                .get()
                .uri(searchInfo.getUri()+"?keyword={keyword}", searchInfo.getKeyword())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(3000))
                .onErrorResume(e -> {
                    log.error(">> WebClient Call Error >> type : {}, keyword : {}, uri : {}, message : {}", searchInfo.getType(), searchInfo.getKeyword(), searchInfo.getUri(), e.getMessage());
                    return Mono.empty();
                })
                .map(map -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", searchInfo.getType());
                    result.put("data", map);
                    return result;
                });
    }
/*    private Mono<Map> getWebSearchData(String query) {
        return getSearchResponseMono(new SearchInfo(query, webUri, SearchType.web.name()));
    }
    private Mono<Map> getWeb2SearchData(String query) {
        return getSearchResponseMono(new SearchInfo(query, web2Uri, SearchType.web2.name()));
    }
    private Mono<Map> getWeb3SearchData(String query) {
        return getSearchResponseMono(new SearchInfo(query, web3Uri, SearchType.web3.name()));
    }

    private Mono<Map> getAppSearchData(String query) {
        return getSearchResponseMono(new SearchInfo(query, appUri, SearchType.app.name()));
    }*/

/*
    private Mono<Map> postSearchResponseMono(SearchInfo searchInfo) {
        WebClient webClient = WebClient.builder().build();

        return webClient
                .post()
                .uri(searchInfo.getUri())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new SearchRequest(searchInfo.getKeyword()))
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(3000))
                .onErrorResume(e -> {
                    log.error(">> WebClient Call Error >> type : {}, keyword : {}, uri : {}, message : {}", searchInfo.getType(), searchInfo.getKeyword(), searchInfo.getUri(), e.getMessage());
                    return Mono.empty();
                })
                .map(map -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", searchInfo.getType());
                    result.put("data", map);
                    return result;
                });
    }
*/




}
