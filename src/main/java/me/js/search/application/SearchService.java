package me.js.search.application;

import lombok.extern.slf4j.Slf4j;
import me.js.search.dto.AppSearchResponse;
import me.js.search.dto.ChatBotResponse;
import me.js.search.dto.SearchResponse;
import me.js.search.dto.WebSearchResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchService {

    public SearchResponse getSearchResponse() {
        return new SearchResponse();
    }

    private ChatBotResponse getChatBotData() {
        return new ChatBotResponse();
    }

    private WebSearchResponse getWebSearchData() {
        return new WebSearchResponse();
    }

    private AppSearchResponse getAppSearchData() {
        return new AppSearchResponse();
    }
}
