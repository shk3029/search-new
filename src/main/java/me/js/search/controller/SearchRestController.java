package me.js.search.controller;

import lombok.extern.slf4j.Slf4j;
import me.js.search.application.SearchService;
import me.js.search.dto.SearchRequest;
import me.js.search.dto.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/search")
public class SearchRestController {

    private final SearchService searchService;

    public SearchRestController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestBody final SearchRequest searchRequest) {

        long start = System.nanoTime();
        log.info("SearchRestController : {}", searchRequest);

        SearchResponse searchResponse = searchService.getSearchResponse();

        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("완료 시간 : " + duration + " msecs");

        return ResponseEntity.ok()
                .body(searchResponse);
    }
}











