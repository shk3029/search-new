package me.js.search.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private String keyword;

    public SearchRequest() {
    }

    public SearchRequest(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "keyword='" + keyword + '\'' +
                '}';
    }
}
