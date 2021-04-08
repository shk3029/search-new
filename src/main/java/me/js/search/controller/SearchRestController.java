package me.js.search.controller;

import lombok.extern.slf4j.Slf4j;
import me.js.search.application.SearchService;
import me.js.search.dto.SearchRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchRestController {

    private final SearchService searchService;

    public SearchRestController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> search(@RequestBody SearchRequest searchRequest) {

        long start = System.nanoTime();
        log.info("SearchRestController : {}", searchRequest);

        Map<String, Object> searchResponse = searchService.getSearchResponse(searchRequest);

        long duration = (System.nanoTime() - start) / 1_000_000;

        log.info("완료 시간 : {} msecs", duration);

        return ResponseEntity.ok()
                .body(searchResponse);
    }
}











