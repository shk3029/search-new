package me.js.search.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponse {

    private String type;
    private String query;
    private String data;

    @Override
    public String toString() {
        return "SearchResponse{" +
                "type='" + type + '\'' +
                ", query='" + query + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
