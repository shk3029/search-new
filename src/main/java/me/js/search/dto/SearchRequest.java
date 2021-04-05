package me.js.search.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private String query;

    public SearchRequest() {
    }

    public SearchRequest(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "query='" + query + '\'' +
                '}';
    }
}
