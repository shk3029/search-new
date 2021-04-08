package me.js.search.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchInfo {
    private String keyword;
    private String uri;
    private String type;

    public SearchInfo(String keyword, String uri, String type) {
        this.keyword = keyword;
        this.uri = uri;
        this.type = type;
    }

    @Override
    public String toString() {
        return "SearchInfo{" +
                "keyword='" + keyword + '\'' +
                ", uri='" + uri + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
