package me.js.search.controller;

import lombok.extern.slf4j.Slf4j;
import me.js.search.application.SearchService;
import me.js.search.dto.SearchRequest;
import me.js.search.dto.SearchResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/search")
public class SearchRestController {

    private final SearchService searchService;

    public SearchRestController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<List<SearchResponse>> search(@RequestBody final SearchRequest searchRequest) {

        long start = System.nanoTime();
        log.info("SearchRestController : {}", searchRequest);

        List<SearchResponse> searchResponse = searchService.getSearchResponse(searchRequest);

        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("완료 시간 : " + duration + " msecs");

        return ResponseEntity.ok()
                .body(searchResponse);
    }
}











